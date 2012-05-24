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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Column {

    private String name;
    private String label;
    private String description;
    private String lookup;    
    //private TableModel model;
    private CellEditorType editorType = null;
    private final Integer resizeModeValue;

    public Column(String name, String label, String description, String lookup, Integer resizeModeValue){
        this.name = name;
        this.label = label;
        this.description = description;
        this.lookup = lookup;
        this.resizeModeValue = resizeModeValue;
    }
    
    public Column(String name, String label, String description, String lookup){
        this(name, label, description, lookup, 0);
    }

    public Column(String name, String label, String description){
        this(name, label, description, null, 0);
    }

    public Item bind(Object entity) {
        
        /* getter */
        Method getter = Resolver.getterFromFieldName(entity.getClass(), this.name);
        Class<?> returnType = getter.getReturnType();
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
        Method setter = Resolver.setterFromFieldName(entity.getClass(), this.name, returnType);
        if( returnType == String.class ){
            ItemField item = new ItemField(this, result, setter, entity);
            this.editorType = CellEditorType.STRING;
            return item;
        } else if( returnType == Integer.class ){
            IntegerItemField item = new IntegerItemField(this, result, setter, entity);
            this.editorType = CellEditorType.INTEGER;
            return item;
        } else if( returnType == Boolean.class ){
            BooleanItemField item = new BooleanItemField(this, result, setter, entity);
            this.editorType = CellEditorType.BOOLEAN;
            return item;
        } else if( returnType == Date.class ){
            DateItemField item = new DateItemField(this, result, setter, entity);
            this.editorType = CellEditorType.DATE;
            return item;
        } else if( returnType.isEnum() ) {
            List choices = new ArrayList();
            choices.addAll(Arrays.asList(returnType.getEnumConstants()));
            ChoiceItemField item = new ChoiceItemField(this, result, setter, entity, choices);
            this.editorType = CellEditorType.CHOICE;
            return item;
        } else {
            LookupItemField item = new LookupItemField(this, result, setter, entity,
                    this.lookup, returnType.getName());
            this.editorType = CellEditorType.LOOKUP;
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

    public CellEditorType getEditorType() {
        return editorType;
    }

    public Integer getResizeModeValue() {
        return resizeModeValue;
    }


}
