/*
 * Copyright (C) 2013 AXIA Studio (http://www.axiastudio.com)
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
 * You should have received a copy of the GNU Afffero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.pypapi;

import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.ICriteriaFactory;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.demo.entities.Book;
import com.axiastudio.pypapi.demo.entities.Genre;
import com.axiastudio.pypapi.ui.CellEditorType;
import com.axiastudio.pypapi.ui.Column;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class CriteriaTest {
    
    private static EntityManagerFactory emf;
    private EntityManager em;
    private Controller bookController;
    
    public CriteriaTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("DemoPU");
        try {
            // register predicate provider
            Method provider = PredicateProvider.class.getMethod("bookPredicateProvider", CriteriaQuery.class, CriteriaBuilder.class);
            Register.registerUtility(provider, ICriteriaFactory.class, Book.class.getName());
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(CriteriaTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(CriteriaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        em = emf.createEntityManager();
        this.bookController = new Controller(emf, Book.class);
    }
    
    @After
    public void tearDown() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Book b").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }
    
    private void insertTestData() {
        
        Book book1 = new Book();
        book1.setTitle("Anna Karenina");
        book1.setDescription("A very long romance...");
        book1.setGenre(Genre.ROMANCE);
        book1.setYear(1877);
        Book book2 = new Book();
        book2.setTitle("Thinking in Java (4th Edition)");
        book2.setDescription("A Java book");
        book2.setGenre(Genre.REFERENCE);
        book2.setYear(2003);
        Book book3 = new Book();
        book3.setTitle("Shantaram");
        book3.setDescription("Another very long romance...");
        book3.setGenre(Genre.ROMANCE);
        book3.setYear(2003);
        Book book4 = new Book();
        book4.setTitle("De bello gallico");
        book4.setDescription("I do not like history...");
        book4.setGenre(Genre.HISTORY);
        
        em.getTransaction().begin();
        em.persist(book1);
        em.persist(book2); // filtered by the predicates provider
        em.persist(book3);
        em.persist(book4);
        em.getTransaction().commit();
        
    }
    
    @Test
    public void testController() {
        Store store = this.bookController.createFullStore();
        assert store.toArray().length == 0;
        this.insertTestData();
        store = this.bookController.createFullStore();
        assert store.toArray().length == 3; // book2 is filtered
    }
    
    @Test
    public void testCriteria() {
        this.insertTestData();
        Map<Column, Object> criteriaMap = new HashMap();
        Column genre = new Column("year", "year", "year");
        genre.setEditorType(CellEditorType.INTEGER);
        criteriaMap.put(genre, 2003);
        Store store = this.bookController.createCriteriaStore(criteriaMap);
        assert store.toArray().length == 1;
    }
        
}