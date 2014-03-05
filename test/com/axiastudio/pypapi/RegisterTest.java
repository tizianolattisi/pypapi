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

import com.axiastudio.pypapi.annotations.Adapter;
import org.junit.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */

interface ITestInterface {}

class TestToAdaptClass {}

class TestAdaptedClass{}

class TestClassWithAdapter {
    @Adapter
    public TestAdaptedClass adapterMethod1(TestToAdaptClass toAdapt){
        return new TestAdaptedClass();
    }
    @Adapter
    public TestAdaptedClass adapterMethod2(TestToAdaptClass toAdapt){
        return new TestAdaptedClass();
    }
    public void nonAdapterMethod(){
        /* nothing to do */
    }
}

class TestRelatedClass {}

class TestObjectClass {}

public class RegisterTest {
        
    public RegisterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testRegisterAndQueryUtility() {        
        /* base utility */
        String utility1="Fake utility 1";
        Register.registerUtility(utility1, ITestInterface.class);
        assert Register.queryUtility(ITestInterface.class) == utility1;
        /* named utility */
        String utility2="Fake utility 2";
        Register.registerUtility(utility2, ITestInterface.class, "fake.prefix.fakename");
        assert Register.queryUtility(ITestInterface.class) != utility2;
        assert Register.queryUtility(ITestInterface.class, "fake.prefix.wrongname") == null;
        assert Register.queryUtility(ITestInterface.class, "fake.prefix.fakename") == utility2;
        /* no prefix */
        assert Register.queryUtility(ITestInterface.class, "fakename") != utility2;
        assert Register.queryUtility(ITestInterface.class, "fakename", true) == utility2;
    }
    
    @Test
    public void testRegisterAndQueryAdapter() throws NoSuchMethodException {
        /* adapter from class*/
        Method adapterMethod1 = TestClassWithAdapter.class.getMethod("adapterMethod1", TestToAdaptClass.class);
        Register.registerAdapter(adapterMethod1, TestToAdaptClass.class, TestAdaptedClass.class);
        assert adapterMethod1 == Register.queryAdapter(TestToAdaptClass.class, TestAdaptedClass.class);
        /* adapter from classes list */
        Method adapterMethod2 = TestClassWithAdapter.class.getMethod("adapterMethod2", TestToAdaptClass.class);
        List adaptsList = new ArrayList();
        adaptsList.add(TestToAdaptClass.class);
        Register.registerAdapter(adapterMethod2, adaptsList, TestAdaptedClass.class);
        assert adapterMethod2 == Register.queryAdapter(TestToAdaptClass.class, TestAdaptedClass.class);
    }
    
    @Test
    public void testRegisterAndQueryRelation() {
        TestRelatedClass related = new TestRelatedClass();
        TestObjectClass object = new TestObjectClass();        
        Register.registerRelation(related, object, "relationName");
        assert Register.queryRelation(object, "relationName") == related;
    }
    
}
