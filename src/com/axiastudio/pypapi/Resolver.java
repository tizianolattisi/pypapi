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
import com.axiastudio.pypapi.annotations.Private;
import com.axiastudio.pypapi.annotations.Callback;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Resolver class
 * 
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 * 
 */
public class Resolver {
    
    public static Object entityFromReference(Object parent, String reference){
        Object referenced = null;
        try {
            String getterName = "get" + reference.substring(0,1).toUpperCase() +
            reference.substring(1);
            Method m = parent.getClass().getMethod(getterName);
            referenced = m.invoke(parent);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(Resolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        return referenced;
    }

    public static Class collectionClassFromReference(Class parent, String reference){
        String methodName = "get"+reference.substring(0,1).toUpperCase()+reference.substring(1);
        Method method = null;
        try {
            method = parent.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(Resolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        ParameterizedType pt = (ParameterizedType) method.getGenericReturnType();
        Type[] actualTypeArguments = pt.getActualTypeArguments();
        Class collectionClass = (Class) actualTypeArguments[0];

        return collectionClass;
    }
    
    public static Class entityClassFromReference(Class parent, String reference){
        String methodName = "get"+reference.substring(0,1).toUpperCase()+reference.substring(1);
        Method entityMethod = null;
        try {
            entityMethod = parent.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(Resolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        Class entityClass = (Class) entityMethod.getGenericReturnType();

        return entityClass;
        }
    
    public static Class<?> interfaceFromEntityClass(Class entityClass){
        Class<?> iface = null;
        Class<?>[] interfaces = entityClass.getInterfaces();
        for( Class<?> i: entityClass.getInterfaces() ){
            iface = i;
        }
        return iface;
    }
    
    public static List<Method> adaptersFromClass(Class klass){
        return Resolver.annotatedFromClass(klass, Adapter.class);
    }

    public static List<Method> callbacksFromClass(Class klass){
        return Resolver.annotatedFromClass(klass, Callback.class);
    }

    public static List<Method> privatesFromClass(Class klass){
        return Resolver.annotatedFromClass(klass, Private.class);
    }

    /**
     * Return tha list of annotated method of a class
     * 
     * @param klass The class
     * @param annotation The annotation to search
     * @return  The list of methods
     * 
     */    
    private static List<Method> annotatedFromClass(Class klass, Class annotation){
        List<Method> annotated = new ArrayList();
        for (Method m : klass.getMethods()) {
            if (m.isAnnotationPresent(annotation)) {
                annotated.add(m);
            }
        }
        return annotated;
    }

    /**
     * Find the getter method of a Serializable from the field name.
     * 
     * @param entityClass The Serializable class
     * @param fieldName The name of the field
     * @return  The accessory getter method
     * 
     */    
    public static Method getterFromFieldName(Class entityClass, String name){
        String getterName = "get" + name.substring(0,1).toUpperCase() + name.substring(1);
        Method getter = null;
        try {
            getter = entityClass.getMethod(getterName);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(Resolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        return getter;
    }

    /**
     * Find the setter method of a Serializable from the field name.
     * 
     * @param entityClass The Serializable class
     * @param fieldName The name of the field
     * @param valueType The value type of the field
     * @return  The accessory setter method
     * 
     */    
    public static Method setterFromFieldName(Class entityClass, String name, Class<?> valueType){
        String setterName = "set" + name.substring(0,1).toUpperCase() + name.substring(1);
        Method setter = null;
        if( valueType == null ){
            Method getter = Resolver.getterFromFieldName(entityClass, name);
            valueType = getter.getReturnType();
        }
        try {
            setter = entityClass.getMethod(setterName, valueType);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(Resolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        return setter;
    }

    /**
     * Find the setter method of a Serializable from the field name.
     * 
     * @param entityClass The Serializable class
     * @param fieldName The name of the field
     * @return  The accessory setter method
     * 
     */        
    public static Method setterFromFieldName(Class entityClass, String name){
        return Resolver.setterFromFieldName(entityClass, name, null);
    }
    
    /**
     * Discover the setter method fot the given entityClass.
     * 
     * @param entityClass the class
     * @param entityToSet the class of the setter's parameter
     * @return the setter method
     * 
     */
    public static List<Method> settersFromEntityClass(Class entityClass, Class entityToSet){
        List<Method> setters = new ArrayList();
        for (Method m : entityClass.getMethods()) {
            Class<?>[] pars = m.getParameterTypes();
            if( pars.length == 1 && pars[0] == entityToSet ){
                setters.add(m);
            }
        }
        return setters;
    }

}
