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
 * @author AXIA Studio (http://www.axiastudio.it)
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
