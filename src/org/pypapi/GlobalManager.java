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

import java.util.HashMap;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.it)
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
