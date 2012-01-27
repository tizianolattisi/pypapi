/*
 * Copyright (C) 2011 AXIA Studio (http://www.axiastudio.com)
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
package com.axiastudio.pypapi.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author tiziano
 */
public class Column {

    private String name;
    private String label;
    private String description;
    private String lookup;    
    private TableModel model;

    public Column(String name, String label, String description, String lookup){
        this.name = name;
        this.label = label;
        this.description = description;
        this.lookup = lookup;
    }

    public Column(String name, String label, String description){
        this(name, label, description, null);
    }

    public void bindModel(TableModel model){
        this.model = model;
    }

    public Item bind(Object entity) {
        
        /* getter */
        String getterName = "get" + this.name;
        Method getter=null;
        Class<?> returnType = null;
        try {
            getter = entity.getClass().getMethod(getterName);
            returnType = getter.getReturnType();
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Column.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Column.class.getName()).log(Level.SEVERE, null, ex);
        }
        Object result=null;
        try {
            result = getter.invoke(entity);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Column.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Column.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Column.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /* setter */
        String setterName = "set" + this.name;
        Method setter = null;
        try {
            setter = entity.getClass().getMethod(setterName, returnType);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Column.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Column.class.getName()).log(Level.SEVERE, null, ex);
        }

        if( returnType == String.class ){
            ItemField item = new ItemField(this, result, setter, entity);
            return item;
        } else if( returnType == Boolean.class ){
            BooleanItemField item = new BooleanItemField(this, result, setter, entity);
            return item;
        } else {
            ItemLookup item = new ItemLookup(this, result, setter, entity,
                    this.lookup, returnType.getName());
            return item;
        }
    }
    
    public String getDescription() {
        return description;
    }

    public String getLabel() {
        return label;
    }

    public String getLookup() {
        return lookup;
    }

    public String getName() {
        return name;
    }


}
