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
package org.pypapi.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.it)
 */
public class ItemLookup extends ItemEditable {

    private String lookup;
    private String name;

    public ItemLookup(Column column, Object value, Method setterMethod,
            Object entity, String lookup, String name){
        super(column, value, setterMethod, entity);
        this.lookup = lookup;
        this.name = name;
    }
        
    @Override
    public Object getEdit(){
        /*
         * Lookup item uses a lookup property to display itself.
         */
        Object lookupped = null;
        Method m = null;
        Object result = super.get();
        if( result == null ){
            return null;
        }
        try {
            m = result.getClass().getMethod("get" + this.lookup);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(ItemLookup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(ItemLookup.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            lookupped = m.invoke(result);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ItemLookup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ItemLookup.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ItemLookup.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lookupped;
    }

    public String getName() {
        return name;
    }
    
}
