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

import com.trolltech.qt.core.Qt;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class LookupItemField extends ItemField {

    private String lookup;
    private String name;

    public LookupItemField(Column column, Object value, Method setterMethod,
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
            Logger.getLogger(LookupItemField.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(LookupItemField.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            lookupped = m.invoke(result);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(LookupItemField.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(LookupItemField.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(LookupItemField.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lookupped;
    }

    public String getName() {
        return name;
    }
    
    @Override
    protected Qt.ItemFlags getFlags() {
        Qt.ItemFlags flags = Qt.ItemFlag.createQFlags();
        flags.set(Qt.ItemFlag.ItemIsSelectable);
        flags.set(Qt.ItemFlag.ItemIsEnabled);
        return flags;
    }

}
