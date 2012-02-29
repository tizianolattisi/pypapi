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

import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.IController;
import com.axiastudio.pypapi.db.IFactory;
import com.axiastudio.pypapi.ui.Form;
import com.axiastudio.pypapi.ui.IForm;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 * 
 * The Register class provides method for register and query utility and
 * adapters.
 * 
 */
public class Register {
    
    private static HashMap utilities = new HashMap();
    private static HashMap adapters = new HashMap();
    private static HashMap relations = new HashMap();

    /**
     * Registers the unnamed utility for the given interface.
     * 
     * @param utility The utility object to register
     * @param iface The interface implemented by the utility
     * 
     */
    public static void registerUtility(Object utility, Class iface){
        Register.registerUtility(utility, iface, ".");

    }

    /**
     * Registers the named utility for the given interface.
     * 
     * @param utility The utility object to register
     * @param iface The interface implemented by the utility
     * @param name The string name
     * 
     */
    public static void registerUtility(Object utility, Class iface, String name){
        HashMap hm;
        Object hmObject = Register.utilities.get(iface);
        if( hmObject != null ){
            hm = (HashMap) hmObject;
        } else {
            hm = new HashMap();
        }
        hm.put(name, utility);
        Register.utilities.put(iface, hm);
    }

    /**
     * Query the unnamed utility with the given interface.
     * 
     * @param iface The interface implemented by the utility
     * @return  The utility
     * 
     */
    public static Object queryUtility(Class iface){
        return Register.queryUtility(iface, ".");
    }

    /**
     * Query the named utility with the given interface.
     * 
     * @param iface The interface implemented by the utility
     * @param name The string name
     * @param noPrefix The string name has no prefix (aka. "name" instead of "pre.name")
     * @return  The utility
     * 
     */
    public static Object queryUtility(Class iface, String name, Boolean noPrefix){
        HashMap hm;
        Object utility = null;
        Object hmObject = Register.utilities.get(iface);
        if( hmObject != null ){
            hm = (HashMap) hmObject;
            if (noPrefix == false){
                utility = hm.get(name);
            } else {
                for (Object o: hm.keySet()){
                    String key = (String) o;
                    if( key.endsWith(name) ){
                        utility = hm.get(key);
                    }
                }
            }
        }
        return utility;
    }

    /**
     * Query the named utility with the given interface (default with prefix)
     * 
     * @param iface The interface implemented by the utility
     * @param name The string name
     * @return  The utility
     * 
     */
    public static Object queryUtility(Class iface, String name){
        return Register.queryUtility(iface, name, Boolean.FALSE);
    }

    /**
     * Registers adapters from a list of methods. This is useful in conjunction
     * with Resolver.adaptersFromEntityClass method.
     * 
     * @param adatpers  The list of adapter methods
     */
    public static void registerAdapters(List<Method> methods){
        for(Method adapter: methods){
            Class<?> toClass = adapter.getReturnType();
            Class<?>[] parameterTypes = adapter.getParameterTypes();
            Class<?> fromClass = null;
            if( parameterTypes.length == 1){
                fromClass = parameterTypes[0];
            }
            if( fromClass != null ){
                Register.registerAdapter(adapter, fromClass, toClass);
            }
        }
    }
    
    /**
     * Registers a named adapter for a list of interfaces.
     * 
     * @param adatper  The adapter itself
     * @param adapts The list of interfaces that the adapter adapts
     * @param provides The interface that the adapter implements
     * @param name The string name 
     */
    public static void registerAdapter(Object adatper, List<Class> adapts, Class provides, String name){
        HashMap hm;
        for (Class c: adapts ){
            Object hmObject = Register.adapters.get(c);
            if( hmObject != null ){
                hm = (HashMap) hmObject;
            } else {
                hm = new HashMap();
            }
            hm.put(provides, adatper);
            Register.adapters.put(c, hm);
        }
    }

    /**
     * Registers an adapter for a list of interfaces.
     * 
     * @param adatper The adapter itself
     * @param adapts The list of interfaces that the adapter adapts
     * @param provides The interface that the adapter implements
     */
    public static void registerAdapter(Object adatper, List<Class> adapts, Class provides){
        Register.registerAdapter(adatper, adapts, provides, null);
    }


    /**
     * Registers an adapter for a single interface.
     * 
     * @param adapter The adapter object
     * @param adapts The class or interface that the adapter adapts
     * @param provides The class or interface that the adapter implements
     */
    public static void registerAdapter(Object adapter, Class adapts, Class provides){
        List adaptsList = new ArrayList();
        adaptsList.add(adapts);
        Register.registerAdapter(adapter, adaptsList, provides);
    }

    /**
     * Query an adapter.
     * 
     * @param adapts The class or interface of the adapted object
     * @param provides The class or interface that the adapter should implement
     * @return  The adapter
     */
    public static Object queryAdapter(Class adapts, Class provides){
        HashMap hm;
        Object adapter = null;
        Object hmObject = Register.adapters.get(adapts);
        if( hmObject != null ){
            hm = (HashMap) hmObject;
            adapter = hm.get(provides);
        }
        return adapter;
    }

    /**
     * Registers a named relation.
     * 
     * @param related The related object
     * @param object The object to which connect the related object
     * @param name The string name
     * 
     */
    public static void registerRelation(Object related, Object object, String name){
        HashMap hm;
        Object hmObject = Register.relations.get(object);
        if( hmObject != null ){
            hm = (HashMap) hmObject;
        } else {
            hm = new HashMap();
        }
        hm.put(name, related);
        Register.relations.put(object, hm);
    }

    /**
     * Query a related object.
     * 
     * @param object 
     * @param name The string name
     * @return The object relation
     * 
     */
    public static Object queryRelation(Object object, String name){
        HashMap hm;
        Object related = null;
        Object hmObject = Register.relations.get(object);
        if( hmObject != null ){
            hm = (HashMap) hmObject;
            related = hm.get(name);
        }
        return related;
    }

    /**
     * Registers controller, factory and form.
     * 
     * @param emf The entity manager factory
     * @param ui The classpath of the ui form
     * @param factory The class factory
     * 
     */
    public static Form registerForm(EntityManagerFactory emf, String ui, Class factory){
        return Register.registerForm(emf, ui, factory, "");
    }        

    /**
     * Registers controller, factory and form.
     * 
     * @param emf The entity manager factory
     * @param ui The classpath of the ui form
     * @param factory The class factory
     * @param title The title of the form
     * 
     */
    public static Form registerForm(EntityManagerFactory emf, String ui, Class factory, String title){        
        Controller controller = new Controller(emf, factory);
        Register.registerUtility(controller, IController.class, factory.getName());
        Form form = new Form(ui, factory, title);
        Register.registerUtility(form, IForm.class, factory.getName());
        Register.registerUtility(factory, IFactory.class, factory.getName());
        form.init();
        return form;
    }

}
