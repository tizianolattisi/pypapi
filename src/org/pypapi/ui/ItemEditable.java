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
public class ItemEditable extends Item {
    
    private Method setter;
    private Object parentEntity;

    public ItemEditable(Column column, Object value, Method setterMethod,
            Object entity){
        super(column, value);
        this.parentEntity = entity;
        this.setter = setterMethod;
    }

    public boolean setRoleValue(int role, Object objValue) throws Exception {
        /*
         * Set the value corresponding to the requested role
         * thru the correct setter method.
         */

        Object result;

        Object nameObject = ItemEditable.ITEM_ROLES.get(role);
        if( nameObject == null){
            return false;
        }
        String name = (String) nameObject;
        String setterName = "set" + name.substring(0,1).toUpperCase() +
                name.substring(1);
        Method m = this.getClass().getMethod(setterName, Object.class);
        result = m.invoke(this, objValue);
        return true;
    }

    public boolean set(Object objValue){
        try {
            Object res = this.setter.invoke(parentEntity, objValue);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ItemEditable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ItemEditable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ItemEditable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public Object getEdit(){
        Object result = this.value;
        if( result == null ){
            result = "n.d.";
        }
        return result;
    }
    
    public boolean setEdit(Object objValue){
        boolean res = this.set(objValue);
        return res;
    }

}
