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
import java.util.Collection;
import java.util.List;


/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */

interface IEntityClass {};


class EntityClass implements IEntityClass {
    ReferencedClass referenced = new ReferencedClass();
    Collection<ReferencedClass> collection = null;

    public void setReferenced(ReferencedClass referenced) {
        this.referenced = referenced;
    }

    public Collection<ReferencedClass> getCollection() {
        return collection;
    }
    
    public void EntityClass(){
        this.collection = new ArrayList();
        this.collection.add(new ReferencedClass());
        this.collection.add(new ReferencedClass());
    }
    
    public ReferencedClass getReferenced(){
        return this.referenced;
    }
    
    @Adapter
    public AdaptedClass classAdapter(EntityClass entity){
        return new AdaptedClass();
    }
}

class ReferencedClass{}

class AdaptedClass{}

public class ResolverTest {
    
    public ResolverTest() {
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
    public void testEntityFromReference() {
        EntityClass entity = new EntityClass();
        assert entity.referenced == Resolver.entityFromReference(entity, "referenced");
    }
    
    @Test
    public void testCollectionClassFromReference(){
        assert ReferencedClass.class == Resolver.collectionClassFromReference(EntityClass.class, "collection");
    }
    
    @Test
    public void testEntityClassFromReference(){
        assert ReferencedClass.class == Resolver.entityClassFromReference(EntityClass.class, "referenced");
    }
    
    @Test
    public void testInterfaceFromEntityClass(){
        assert IEntityClass.class == Resolver.interfaceFromEntityClass(EntityClass.class);
    }
    
    @Test
    public void testAdaptersFromEntityClass() throws NoSuchMethodException{
        Method method = EntityClass.class.getMethod("classAdapter", EntityClass.class);
        assert Resolver.adaptersFromClass(EntityClass.class).contains(method);
    }
    
    @Test
    public void testGetterFromFieldName() throws NoSuchMethodException{
        Method method = Resolver.getterFromFieldName(EntityClass.class, "referenced");
        assert method.equals(EntityClass.class.getMethod("getReferenced"));
    }

    @Test
    public void testSetterFromFieldName() throws NoSuchMethodException{
        /* without value type */
        Method method1 = Resolver.setterFromFieldName(EntityClass.class, "referenced");
        assert method1.equals(EntityClass.class.getMethod("setReferenced", ReferencedClass.class));
        /* with value type */
        Method method2 = Resolver.setterFromFieldName(EntityClass.class, "referenced", ReferencedClass.class);
        assert method2.equals(EntityClass.class.getMethod("setReferenced", ReferencedClass.class));
    }

    @Test
    public void testSettersFromEntityClass() throws NoSuchMethodException{
        List<Method> setters = Resolver.settersFromEntityClass(EntityClass.class, ReferencedClass.class);
        assert setters.size() == 1;
        assert setters.get(0).equals(EntityClass.class.getMethod("setReferenced", ReferencedClass.class));
    }

}
