/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pypapi.db;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.Session;

import org.pypapi.GlobalManager;


/**
 *
 * @author tiziano
 */
public class Database implements IDatabase {

    private SessionFactory sessionFactory;

    @Override
    public void open() {
        GlobalManager.registerUtility(this, IDatabase.class);
        try {
            this.sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Session createNewSession() {
        Session session=null;
        try {
            session = sessionFactory.openSession();
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return session;
    }
}
