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
package com.axiastudio.pypapi.demo;

import com.axiastudio.pypapi.Application;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.Resolver;
import com.axiastudio.pypapi.db.Database;
import com.axiastudio.pypapi.demo.entities.*;
import com.axiastudio.pypapi.ui.Dialog;
import com.axiastudio.pypapi.ui.Window;
import com.axiastudio.pypapi.ui.Util;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Demo {
        public static void main(String[] args) {
        
        /* Initialize demo data */
        Database db = new Database();
        db.open("DemoPU");
        EntityManagerFactory emf = db.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
                
        Book book = new Book();
        book.setTitle("Anna Karenina");
        book.setDescription("description...");
        book.setGenre(Genre.ROMANCE);

        Book book2 = new Book();
        book2.setTitle("Pro JPA2");
        book2.setDescription("Mastering the Java Persistence API");
        book2.setGenre(Genre.REFERENCE);
        
        Person person = new Person();
        person.setName("Tiziano");
        person.setSurname("Lattisi");
        
        Reference r = new Reference();
        r.setReferencetype(Referencetype.EMAIL);
        r.setReferencevalue("tiziano (at) axiastudio.it");
        
        Collection<Reference> referenceCollection = new ArrayList();
        referenceCollection.add(r);

        person.setReferenceCollection(referenceCollection);

        Loan l = new Loan();
        l.setBook(book);
        Loan l2 = new Loan();
        l2.setBook(book2);
        
        Collection<Loan> loanCollection = new ArrayList();
        loanCollection.add(l);
        loanCollection.add(l2);
        
        person.setLoanCollection(loanCollection);

        em.getTransaction().begin();
        em.persist(book);
        em.persist(book2);
        em.persist(person);
        em.getTransaction().commit();
        em.close();
        
        /* start demo */
        Application app = new Application(args);
        app.setLanguage("en");

        // Book
        Register.registerForm(db.getEntityManagerFactory(),
                            "classpath:com/axiastudio/pypapi/demo/forms/book.ui",
                            Book.class);
        // Person
        Register.registerForm(db.getEntityManagerFactory(),
                            "classpath:com/axiastudio/pypapi/demo/forms/person.ui",
                            Person.class);
        // Loan
        Register.registerForm(db.getEntityManagerFactory(),
                            "classpath:com/axiastudio/pypapi/demo/forms/loan.ui",
                            Loan.class,
                            Dialog.class);
        
        // Adapter
        // note: you can comment the line because the framework can inspect and
        // find the adapter itself.
        Register.registerAdapters(Resolver.adaptersFromClass(Book.class));
        
        // Query and show the form
        Window form = Util.formFromName(Person.class.getName());
        form.show();
        int exec = app.exec();
        
    }
}
