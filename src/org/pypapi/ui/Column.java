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


/**
 *
 * @author AXIA Studio (http://www.axiastudio.it)
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

    public Item bind(Object entity) throws Exception {
        
        /* getter */
        String getterName = "get" + this.name;
        Method getter = entity.getClass().getMethod(getterName);
        Object result=null;
        result = getter.invoke(entity);
        
        /* setter */
        String setterName = "set" + this.name;
        Method setter = entity.getClass().getMethod(setterName, result.getClass());

        if( result.getClass() == String.class ){
            ItemEditable item = new ItemEditable(this, result, setter, entity);
            return item;
        } else {
            ItemLookup item = new ItemLookup(this, result, setter, entity, this.lookup);
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
