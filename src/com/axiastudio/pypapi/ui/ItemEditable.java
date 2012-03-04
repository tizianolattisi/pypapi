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
package com.axiastudio.pypapi.ui;

import com.axiastudio.pypapi.Resolver;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class ItemEditable extends Item {
    
    private Method setterMethod;
    private Object entity;

    public ItemEditable(Column column, Object value, Method setterMethod,
            Object entity){
        super(column, value);
        this.entity = entity;
        this.setterMethod = setterMethod;
    }

    public boolean set(int role, Object objValue) throws Exception {
        /*
         * Set the value corresponding to the requested role
         * thru the correct setter method.
         */
        // XXX: chek ItemEditable.set(role, value) -> Item*.set*(value) -> ItemEditable.set(value)
        Object result;
        Object nameObject = ItemEditable.ITEM_ROLES.get(role);
        if( nameObject == null){
            return false;
        }
        String name = (String) nameObject;
        Method setter = Resolver.setterFromFieldName(this.getClass(), name, Object.class);
        result = setter.invoke(this, objValue);
        return true;
    }

    public boolean set(Object objValue){
        try {
            Object res = this.setterMethod.invoke(entity, objValue);
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
