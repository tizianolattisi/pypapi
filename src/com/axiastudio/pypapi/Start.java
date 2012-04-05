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
package com.axiastudio.pypapi;

import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.demo.entities.Book;
import com.axiastudio.pypapi.ui.Form;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Start {
    
    public static void main(String[] args) {
        
        /*
        Application.initialize(args);
        Util.warningBox(null, "PyPaPi Qt Java Frmework", "Ok");
        Application.exec();
        */

        /* Initialize demo data */
        Database db = new Database();
        db.open("DemoPU");
        EntityManagerFactory emf = db.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        
        Book book = new Book();
        book.setTitle("Anna Karenina");
        book.setDescription("description...");
        
        em.getTransaction().begin();
        em.persist(book);
        em.getTransaction().commit();
        em.close();
        
        /* start demo */
        Application.initialize(args);

        Form formBook = Register.registerForm(db.getEntityManagerFactory(),
                            "classpath:com/axiastudio/pypapi/demo/forms/book.ui",
                            Book.class);
        formBook.show();
        
        Application.exec();
        
 
    }
}
