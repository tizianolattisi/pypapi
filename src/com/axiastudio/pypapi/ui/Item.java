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
import com.trolltech.qt.core.Qt;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Item {
    
    public static final Map<Integer, String> ITEM_ROLES;
    static {
        ITEM_ROLES = new HashMap<Integer, String>();
        ITEM_ROLES.put(Qt.ItemDataRole.DisplayRole, "Display");
        ITEM_ROLES.put(Qt.ItemDataRole.EditRole, "Edit");
        ITEM_ROLES.put(Qt.ItemDataRole.CheckStateRole, "Checkstate");
    }

    protected final Column column;
    protected final Object value;

    //protected static Map itemRolesNameMap = new HashMap();

    public Item(Column column, Object value){
        this.column = column;
        this.value = value;
    }

    public Object get(int role) {
        /*
         * Get to the value corresponding to the requested role
         * (like DisplayRole, EditRole, etc) thru the correct getter method.
         */

        Object result=null;

        Object nameObject = ITEM_ROLES.get(role);
        if( nameObject == null){
            return null;
        }
        String name = (String) nameObject;
        Method getter = Resolver.getterFromFieldName(this.getClass(), name);
        try {
            result = getter.invoke(this);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            String msg = "Unable to invoke getter for attribute '" + column.getName() +"'";
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, msg, ex);
        }
        return result;
    }


    public Object get(){
        return this.value;
    }


    public Object getDisplay(){
        Object result = this.value;
        if( result == null ){
            result = "";
        }
        return result;
    }
    
    public Object getEdit(){
        Object result = this.value;
        if( result == null ){
            result = "";
        }
        return result;
    }
        
    public Qt.CheckState getCheckstate(){
        return null;
    }

}
