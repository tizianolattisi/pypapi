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

import com.axiastudio.pypapi.ui.widgets.PyPaPiTableView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Resolver {
    
    public static Object entityFromReference(Object parent, String reference){
        Object referenced = null;
        try {
            String name = (String) reference;
            String getterName = "get" + name.substring(0,1).toUpperCase() +
            name.substring(1);
            Method m = parent.getClass().getMethod(getterName);
            referenced = m.invoke(parent);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Resolver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Resolver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Resolver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Resolver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Resolver.class.getName()).log(Level.SEVERE, null, ex);
        }
        return referenced;
    }

    public static Class collectionClassFromReference(Class parent, String reference){
        String methodName = "get"+reference.substring(0,1).toUpperCase()+reference.substring(1);
        Method method = null;
        try {
            method = parent.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Resolver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
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
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Resolver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
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
    
}
