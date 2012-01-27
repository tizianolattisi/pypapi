/*
 * Copyright (C) 2011 AXIA Studio (http://www.axiastudio.it)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.pypapi.db;

import java.io.Serializable;
import java.util.List;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.axiastudio.pypapi.ui.Column;


/**
 *
 * @author AXIA Studio (http://www.axiastudio.it)
 */
public class Controller implements IController {
    
    private Serializable jpaController;
    private String className;
    private String entityName;

    public Controller(Serializable jpaController, String className){
        this.jpaController = jpaController;
        this.className = className;
        String[] split = className.split("\\.");
        this.entityName = split[split.length-1];
    }

    public EntityManager getEntityManager() {
        EntityManager em=null;
        try {
            Method m;
            m = this.jpaController.getClass().getMethod("getEntityManager");
            em = (EntityManager) m.invoke(this.jpaController);
            return em;
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
        return em;
    }
    
    public Store createCriteriaStore(HashMap criteria){
        Store store=null;
        EntityManager em = this.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery();

        Method m = null;
        try {
            m = this.jpaController.getClass().getMethod("find"+this.entityName, Long.class);
        } catch (NoSuchMethodException ex) {
            try {
                m = this.jpaController.getClass().getMethod("find"+this.entityName, Integer.class);
            } catch (NoSuchMethodException ex2) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex2) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SecurityException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        Class<?> returnType = m.getReturnType();
        Root from = cq.from(returnType);
        CriteriaQuery<Object> select = cq.select(from);
        for( Object k: criteria.keySet() ){
            Column column = (Column) k;
            String value = (String) criteria.get(column);
            // TODO: % and * should be are whildchars
            Predicate predicate = cb.like(from.get(column.getName().toLowerCase()), value);
            cq = cq.where(predicate);
        }
        TypedQuery<Object> tq = em.createQuery(cq);
        List<Object> result = tq.getResultList();
        store = new Store(result);
        return store;
    }
    
    @Override
    public Store createFullStore(){
        try {
            Method m;
            String methodName = "find" + this.entityName + "Entities";
            m = this.jpaController.getClass().getMethod(methodName);
            List entities = (List) m.invoke(this.jpaController);
            Store store = new Store(entities);
            return store;
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
        return null;
    }

    @Override
    public void edit(Object entity){
        try {
            Method m = this.jpaController.getClass().getMethod("edit", entity.getClass());
            m.invoke(this.jpaController, entity);
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
    }

    public void create(Object entity){
        try {
            Method m = this.jpaController.getClass().getMethod("create", entity.getClass());
            m.invoke(this.jpaController, entity);
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
    }
    
    @Override
    public Object refresh(Object entity){
        try {
            Method m = this.jpaController.getClass().getMethod("getEntityManager");
            EntityManager em = (EntityManager) m.invoke(this.jpaController);
            // XXX: controller refresh... does not work :-(
            em.refresh(em.merge(entity));
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
        return entity;
    }
    
    public String getEntityName() {
        return entityName;
    }
    

    public String getClassName() {
        return className;
    }

}
