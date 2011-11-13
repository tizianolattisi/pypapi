/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pypapi.db;

import java.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.pypapi.GlobalManager;


/**
 *
 * @author tiziano
 */
public class Database implements IDatabase {

    private EntityManagerFactory entityManagerFactory;

    @Override
    public void open() {
        GlobalManager.registerUtility(this, IDatabase.class);
        try {
            this.entityManagerFactory = Persistence.createEntityManagerFactory("DemoPU");
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // XXX: I need a sesione provider with criteria support
    @Override
    public Store createStore(Class klass) {
        String[] temp;
        EntityManager entityManager =  this.entityManagerFactory.createEntityManager();
        temp = klass.getName().split("\\.");
        String name = temp[temp.length-1];
        Query q = entityManager.createQuery("select object(o) from "+name+" as o");
        List entities = q.getResultList();
        Store store = new Store(entities);
        return store;
    }

}
