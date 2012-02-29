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

import com.axiastudio.pypapi.ui.Column;
import java.util.HashMap;
import java.util.List;
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

    public Controller(EntityManagerFactory emf, Class entityClass){
        this.entityClass = entityClass;
        this.entityManagerFactory = emf;
    }

    public EntityManager getEntityManager() {
        return this.entityManagerFactory.createEntityManager();
    }
    
    public Store createCriteriaStore(HashMap criteria){
        EntityManager em = this.getEntityManager();
        Store store;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object> cq = cb.createQuery();
        Class<?> returnType = this.entityClass;
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
        EntityManager em = this.getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(this.entityClass));
            Query q = em.createQuery(cq);
            List entities = q.getResultList();
            Store store = new Store(entities);
            return store;
        } finally {
            em.close();
        }
    }

    @Override
    public void edit(Object entity){
        EntityManager em = this.getEntityManager();
        em.getTransaction().begin();
        Object res = em.merge(entity);
        em.getTransaction().commit();
    }

    public void create(Object entity){
        EntityManager em = this.getEntityManager();
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
    }
    
    @Override
    public Object refresh(Object entity){
        // TODO: controller refresh method
        return entity;
    }
    
    public String getEntityName() {
        String[] split = this.getClassName().split("\\.");
        return split[split.length-1];
    }
    

    public String getClassName() {
        return this.entityClass.getName();
    }
    
}
