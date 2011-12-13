/*
 * Copyright (C) 2011 AXIA Studio (http://www.axiastudio.it)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.pypapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.it)
 * 
 * The GlobalManager class provides method for register and query utility and
 * adapters.
 * 
 */
public class GlobalManager {
    
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
        GlobalManager.registerUtility(utility, iface, ".");

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
        HashMap hm = null;
        Object hmObject = GlobalManager.utilities.get(iface);
        if( hmObject != null ){
            hm = (HashMap) hmObject;
        } else {
            hm = new HashMap();
        }
        hm.put(name, utility);
        GlobalManager.utilities.put(iface, hm);
    }

    /**
     * Query the unnamed utility with the given interface.
     * 
     * @param iface The interface implemented by the utility
     * @return  The utility
     * 
     */
    public static Object queryUtility(Class iface){
        return GlobalManager.queryUtility(iface, ".");
    }

    /**
     * Query the named utility with the given interface.
     * 
     * @param iface The interface implemented by the utility
     * @param name The string name
     * @return  The utility
     * 
     */
    public static Object queryUtility(Class iface, String name){
        HashMap hm = null;
        Object utility = null;
        Object hmObject = GlobalManager.utilities.get(iface);
        if( hmObject != null ){
            hm = (HashMap) hmObject;
            utility = hm.get(name);
        }
        return utility;
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
        HashMap hm = null;
        for (Class c: adapts ){
            Object hmObject = GlobalManager.adapters.get(c);
            if( hmObject != null ){
                hm = (HashMap) hmObject;
            } else {
                hm = new HashMap();
            }
            hm.put(provides, adatper);
            GlobalManager.adapters.put(c, hm);
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
        GlobalManager.registerAdapter(adatper, adapts, provides, null);
    }


    /**
     * Registers an adapter for a single interface.
     * 
     * @param adapter The adapter object
     * @param adapts The interface that the adapter adapts
     * @param provides The interface that the adapter implements
     */
    public static void registerAdapter(Object adapter, Class adapts, Class provides){
        List adaptsList = new ArrayList();
        adaptsList.add(adapts);
        GlobalManager.registerAdapter(adapter, adaptsList, provides);
    }

    /**
     * Query an adapter.
     * 
     * @param adapts The interface of the adapted object
     * @param provides The interface that the adapter should implement
     * @return  The adapter
     */
    public static Object queryAdapter(Class adapts, Class provides){
        HashMap hm = null;
        Object adapter = null;
        Object hmObject = GlobalManager.adapters.get(adapts);
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
        HashMap hm = null;
        Object hmObject = GlobalManager.relations.get(object);
        if( hmObject != null ){
            hm = (HashMap) hmObject;
        } else {
            hm = new HashMap();
        }
        hm.put(name, related);
        GlobalManager.relations.put(object, hm);
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
        HashMap hm = null;
        Object related = null;
        Object hmObject = GlobalManager.relations.get(object);
        if( hmObject != null ){
            hm = (HashMap) hmObject;
            related = hm.get(name);
        }
        return related;
    }
}
