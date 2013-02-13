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

import com.axiastudio.pypapi.demo.entities.Book;
import com.axiastudio.pypapi.demo.entities.Book_;
import com.axiastudio.pypapi.demo.entities.Genre;
import com.axiastudio.pypapi.demo.entities.Loan;
import com.axiastudio.pypapi.demo.entities.Person;
import com.axiastudio.pypapi.demo.entities.Person_;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class Jpa2Test {
    
    private static EntityManagerFactory emf;
    private EntityManager em;
    private Long aPrimaryKey;
    
    public Jpa2Test() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        emf = Persistence.createEntityManagerFactory("DemoPU");
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        em = emf.createEntityManager();
    }
    
    @After
    public void tearDown() {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Loan l").executeUpdate();
        em.createQuery("DELETE FROM Book b").executeUpdate();
        em.createQuery("DELETE FROM Person p").executeUpdate();
        em.getTransaction().commit();
        em.close();
    }

    private void insertTestData() {
        Book book1 = new Book();
        book1.setTitle("Anna Karenina");
        book1.setDescription("A very long romance...");
        book1.setGenre(Genre.ROMANCE);
        Book book2 = new Book();
        book2.setTitle("Pro JPA2");
        book2.setDescription("Mastering the Java Persistence API");
        book2.setGenre(Genre.REFERENCE);
        
        Person person1 = new Person();
        person1.setName("Tiziano");
        person1.setSurname("Lattisi");
        person1.setLoanCollection(new ArrayList<Loan>());
        Person person2 = new Person();
        person2.setName("Nicola");
        person2.setSurname("Pedot");
        person2.setLoanCollection(new ArrayList<Loan>());
        
        Loan loan1 = new Loan();
        loan1.setBook(book1);
        loan1.setFromdate(new Date());
        Loan loan2 = new Loan();
        loan2.setBook(book2);
        loan2.setFromdate(new Date());

        person1.getLoanCollection().add(loan1);
        person1.getLoanCollection().add(loan2);
        
        em.getTransaction().begin();
        em.persist(book1);
        em.persist(book2);
        em.persist(person1);
        em.persist(person2);
        em.getTransaction().commit();
        
        aPrimaryKey = person1.getId();
    }
        
    @Test
    public void testInsert() {
        
        Book book = new Book();
        book.setTitle("Dubliners");
        book.setDescription("a collection of 15 short stories");
        book.setGenre(Genre.ROMANCE);
        
        em.getTransaction().begin();
        em.persist(book);
        em.getTransaction().commit();
        
    }
    
    @Test
    public void testSelect() {
        this.insertTestData();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        TypedQuery<Book> tq = em.createQuery(cq);
        List<Book> books = tq.getResultList();
        for( Book book: books ){
            switch (book.getDescription()) {
                case "Anna Karenina":
                    assert "ROMANCE".equals(book.getGenre().toString());
                    break;
                case "Pro JPA":
                    assert "REFERENCE".equals(book.getGenre().toString());
                    break;
            }
        }
    }
    
    @Test
    public void testCriteriaSelect() {
        this.insertTestData();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> cq = cb.createQuery(Book.class);
        Root from = cq.from(Book.class);
        // from.get("description")
        Predicate predicate = cb.like(from.get(Book_.description), "%very%");
        cq = cq.where(predicate);
        TypedQuery<Book> tq = em.createQuery(cq);
        
        List<Book> books = tq.getResultList();
        assert books.size() == 1;
        assert "Anna Karenina".equals(books.get(0).getTitle());
        
        Book book = tq.getSingleResult();
        assert "Anna Karenina".equals(book.getTitle());
    }
    
    @Test
    public void testUpdate() {
        this.insertTestData();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root from = cq.from(Person.class);
        cq = cq.where(cb.and(cb.equal(from.get(Person_.name), "Tiziano"),
                             cb.equal(from.get(Person_.surname), "Lattisi")));
        TypedQuery<Person> tq = em.createQuery(cq);
        Person person = tq.getSingleResult();
        
        assert "Tiziano".equals(person.getName());
        assert person.getEmail() == null;
        Long key = person.getId();
        person.setEmail("tiziano.lattisi@gmail.com");

        // 
        Person personByKey = em.find(Person.class, key);
        assert "tiziano.lattisi@gmail.com".equals(personByKey.getEmail());
        
        // detach
        em.detach(person);
        person = em.find(Person.class, key);
        assert ! "tiziano.lattisi@gmail.com".equals(person.getEmail());
        
        person.setEmail("tiziano.lattisi@gmail.com");
        em.getTransaction().begin();
        em.merge(person);
        em.getTransaction().commit();
        Person personByKey2 = em.find(Person.class, key);
        assert "tiziano.lattisi@gmail.com".equals(personByKey2.getEmail());        
    }
    
    @Test
    public void testDelete() {
        this.insertTestData();
        
        Person person = em.find(Person.class, aPrimaryKey);
        
        em.getTransaction().begin();
        em.remove(person);
        em.getTransaction().commit();
        
        person = em.find(Person.class, aPrimaryKey);
        assert person == null;
    }
}