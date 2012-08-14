/*
 * Copyright (C) 2012 AXIA Studio (http://www.axiastudio.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.pypapi.db;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.Resolver;
import com.axiastudio.pypapi.ui.CellEditorType;
import com.axiastudio.pypapi.ui.Column;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Controller implements IController {
    
    private Class entityClass;
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager=null;

    
    public Controller(EntityManagerFactory emf){
        this(emf, null);
    }
    
    public Controller(EntityManagerFactory emf, Class entityClass){
        this.entityClass = entityClass;
        this.entityManagerFactory = emf;
    }

    public EntityManager getEntityManager() {
        return this.entityManagerFactory.createEntityManager();
    }
    
    @Override
    public Store createCriteriaStore(HashMap criteria){
        if( this.entityClass == null ){
            return null;
        }
        EntityManager em = this.getEntityManager();
        Store store;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery();
        Class<?> returnType = this.entityClass;
        Root from = cq.from(returnType);
        CriteriaQuery<Object> select = cq.select(from);
        for( Object k: criteria.keySet() ){
            Column column = (Column) k;
            Predicate predicate=null;
            if( column.getEditorType().equals(CellEditorType.STRING) ){
                // TODO: % and * should be whildchars
                String value = (String) criteria.get(column);
                predicate = cb.like(from.get(column.getName().toLowerCase()), value);
            } else if( column.getEditorType().equals(CellEditorType.INTEGER) ){
                Integer value = (Integer) criteria.get(column);
                predicate = cb.equal(from.get(column.getName().toLowerCase()), value);
            } else if( column.getEditorType().equals(CellEditorType.BOOLEAN) ){
                Boolean value = (Boolean) criteria.get(column);
                predicate = cb.equal(from.get(column.getName().toLowerCase()), value);
            } else if( column.getEditorType().equals(CellEditorType.DATE) ){
                // TODO: complete...
                List values = (List) criteria.get(column);
                GregorianCalendar gcStart = (GregorianCalendar) values.get(0);
                GregorianCalendar gcEnd = new GregorianCalendar();
                gcEnd.set(Calendar.YEAR, gcStart.get(Calendar.YEAR));
                gcEnd.set(Calendar.MONTH, gcStart.get(Calendar.MONTH));
                gcEnd.set(Calendar.DAY_OF_MONTH, gcStart.get(Calendar.DAY_OF_MONTH));
                Integer d = (Integer) values.get(1);
                gcEnd.add(Calendar.DAY_OF_MONTH, d);
                predicate = cb.and(cb.greaterThanOrEqualTo(from.get(column.getName().toLowerCase()), gcStart.getTime()),
                        cb.lessThan(from.get(column.getName().toLowerCase()), gcEnd.getTime()));
            } else if( column.getEditorType().equals(CellEditorType.CHOICE) ){
                Enum value = (Enum) criteria.get(column);
                predicate = cb.equal(from.get(column.getName().toLowerCase()), value);
            }
            if( predicate != null ){
                cq = cq.where(predicate);
            }
        }
        TypedQuery<Object> tq = em.createQuery(cq);
        List<Object> result = tq.getResultList();
        store = new Store(result);
        return store;
    }

    @Override
    public Store createStore() {
        return this.createStore(-1);
    }

    @Override
    public Store createStore(int limit) {
        if( this.entityClass == null ){
            return null;
        }
        EntityManager em = this.getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(this.entityClass));
            Query q = em.createQuery(cq);
            if( limit > 0 ){
                q = q.setMaxResults(limit);
            }
            List entities = q.getResultList();
            Store store = new Store(entities);
            return store;
        } finally {
            em.close();
        }
    }

    @Override
    public Store createFullStore(){
        return this.createStore(-1);
    }

    /*
     * The parentize method hooks the items of the collections to the parent
     * entity.
     */
    private void parentize(Object entity){
        for(Field f: entity.getClass().getDeclaredFields()){
            for( Annotation a: f.getAnnotations()){
                if( a.annotationType().equals(javax.persistence.OneToMany.class) ){
                    String name = f.getName();
                    Method getter = Resolver.getterFromFieldName(entity.getClass(), name);
                    Collection collection=null;
                    try {
                        collection = (Collection) getter.invoke(entity); // orphan collection
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if( collection != null && collection.size() > 0){
                        Method parentSetter = null;
                        for( Method m: a.annotationType().getMethods()){
                           if( "mappedBy".equals(m.getName()) ){
                                String fk=null;
                                try {
                                    fk = (String) m.invoke(a);
                                } catch (IllegalAccessException ex) {
                                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IllegalArgumentException ex) {
                                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (InvocationTargetException ex) {
                                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                parentSetter = Resolver.setterFromFieldName(collection.iterator().next().getClass(), fk, entity.getClass()); // parent setter
                            }
                        }
                        for (Iterator it = collection.iterator(); it.hasNext();) {
                            Object orphan = it.next();
                            try {
                                parentSetter.invoke(orphan, entity);
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalArgumentException ex) {
                                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InvocationTargetException ex) {
                                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public Validation commit(Object entity){
        // XXX: if no CascadeType.ALL?
        this.parentize(entity);
        Method validator = (Method) Register.queryValidator(entity.getClass());
        Validation val = new Validation(true);
        if( validator != null ){
            try {
                val = (Validation) validator.invoke(null, entity);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if( val.getResponse() == true ){
            EntityManager em = this.getEntityManager();
            em.getTransaction().begin();
            Object merged = em.merge(entity);
            em.getTransaction().commit();
            try {
                // TODO: refresh entity
                Method getId = merged.getClass().getMethod("getId");
                Long i = (Long) getId.invoke(merged);
                em.detach(merged);
                merged = em.find(this.entityClass, i);
                em.merge(merged);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            val.setEntity(merged);
            em.close();
        }
        return val;
    }
    
    @Override
    public void delete(Object entity) {
        EntityManager em = this.getEntityManager();
        em.getTransaction().begin();
        Object merged = em.merge(entity);
        em.remove(merged);
        em.getTransaction().commit();
    }
            
    @Override
    public Object refresh(Object entity){
        EntityManager em = this.getEntityManager();
        Long i=null;
        try {
            Method getId = entity.getClass().getMethod("getId");
            i = (Long) getId.invoke(entity);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        if( i != null ){
            em.detach(entity);
            Object merged = em.find(this.entityClass, i);
            em.merge(merged);
            return merged;
        } else {
            return entity;
        }
    }
    
    public String getEntityName() {
        if( this.entityClass == null ){
            return null;
        }
        String[] split = this.getClassName().split("\\.");
        return split[split.length-1];
    }
    

    public String getClassName() {
        if( this.entityClass == null ){
            return null;
        }
        return this.entityClass.getName();
    }

    public Class getEntityClass() {
        return entityClass;
    }
    
    
}
