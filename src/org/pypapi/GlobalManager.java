/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pypapi;

import java.util.HashMap;

/**
 *
 * @author tiziano
 */
public class GlobalManager {
    private static HashMap utilities = new HashMap();

    public static void registerUtility(Object utility, Class iface){
        GlobalManager.registerUtility(utility, iface, ".");

    }

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

    public static Object queryUtility(Class iface){
        return GlobalManager.queryUtility(iface, ".");
    }

    public static Object queryUtility(Class iface, String name){
        HashMap hm = null;
        Object utility = null;
        Object hmObject = GlobalManager.utilities.get(iface);
        if( hmObject != null ){
            hm = (HashMap) hmObject;
            utility = hm.get(name);
        } else {
            utility = null;
        }
        return utility;
    }
}
