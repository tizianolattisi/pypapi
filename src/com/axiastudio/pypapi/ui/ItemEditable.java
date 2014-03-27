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
import com.axiastudio.pypapi.db.Store;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.ItemFlags;

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
         * through the correct setter method.
         */
        // XXX: check ItemEditable.set(role, value) -> Item*.set*(value) -> ItemEditable.set(value)
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
        Store lookupStore = this.column.getLookupStore();
        if( lookupStore != null ){
            objValue = lookupStore.get((Integer) objValue);
        }
        try {
            Object res = this.setterMethod.invoke(this.entity, objValue);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ItemEditable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ItemEditable.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(ItemEditable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }


    
    public boolean setEdit(Object objValue){
        boolean res = this.set(objValue);
        return res;
    }

    protected ItemFlags getFlags() {
        ItemFlags flags = Qt.ItemFlag.createQFlags();
        flags.set(Qt.ItemFlag.ItemIsSelectable);
        flags.set(Qt.ItemFlag.ItemIsEnabled);
        flags.set(Qt.ItemFlag.ItemIsEditable);
        return flags;
    }

}
