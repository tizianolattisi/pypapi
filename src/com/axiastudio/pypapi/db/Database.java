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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Database implements IDatabase {

    private EntityManagerFactory entityManagerFactory;

    public Database() {
    }
    
    public Database(String persistenceUnit) {
        this.open(persistenceUnit);
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    @Override
    public void open(String persistenceUnit) {
        this.open(persistenceUnit, null);
    }
    
    @Override
    public void open(String persistenceUnit, Map properties) {
        Register.registerUtility(this, IDatabase.class);
        try {
            if( properties == null ){
                this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
            } else {
                this.entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit, properties);
            }
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Controller createController(Class klass) {
        Database db = (Database) Register.queryUtility(IDatabase.class);
        Controller controller = new Controller(db.getEntityManagerFactory().createEntityManager(), klass);
        return controller;

    }
}
