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

import java.lang.reflect.Method;
import java.util.*;

import com.trolltech.qt.core.*;

/**
 *
 * @author tiziano
 */
public class Item {
    private final Column column;
    private final Object value;

    protected static Map itemRolesNameMap = new HashMap();

    public Item(Column column, Object value){
        this.column = column;
        this.value = value;

        /* roles map */
        Item.itemRolesNameMap.put(Qt.ItemDataRole.DisplayRole, "display");
        Item.itemRolesNameMap.put(Qt.ItemDataRole.EditRole, "edit");
        Item.itemRolesNameMap.put(Qt.ItemDataRole.CheckStateRole, "checkstate");

    }

    public Object getRoleValue(int role) throws Exception {
        /*
         * Get to the value corresponding to the requested role
         * (like DisplayRole, EditRole, etc) thru the correct getter method.
         */

        Object result;

        Object nameObject = Item.itemRolesNameMap.get(role);
        if( nameObject == null){
            return null;
        }
        String name = (String) nameObject;
        String getterName = "get" + name.substring(0,1).toUpperCase() +
                name.substring(1);
        Method m = this.getClass().getMethod(getterName);
        result = m.invoke(this);
        return result;
    }


    public Object get(){
        return this.value;
    }


    public Object getDisplay(){
        Object result = this.get();
        if( result == null ){
            result = "n.d.";
        }
        return result;
    }

    public Object getEdit(){
        Object result = this.get();
        if( result == null ){
            result = "n.d.";
        }
        return result;
    }

    public Object getCheckstate(){
        return null;
        //return Qt.CheckState.Unchecked;
    }


}
