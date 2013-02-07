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
import com.axiastudio.pypapi.demo.entities.Genre;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class JsonUtilTest {
    
    private static ObjectMapper mapper;
    
    public JsonUtilTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        JsonUtilTest.mapper = new ObjectMapper();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testJsonGenerator() {
        
        Book book = new Book();
        book.setTitle("Anna Karenina");
        book.setDescription("description...");
        book.setGenre(Genre.ROMANCE);
        
        String json = JsonUtil.pojoToJson(book);
        assert json.equals("{\"id\":null,\"title\":\"Anna Karenina\",\"description\":\"description...\",\"genre\":\"ROMANCE\"}");
        
        String updated = "{\"id\":null,\"title\":\"Anna Karenina\",\"description\":\"A very long romance.\",\"genre\":\"ROMANCE\"}";
        JsonUtil.jsonToPojo(book, updated);
        
        assert book.getDescription().equals("A very long romance.");
    }
}