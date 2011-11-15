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

    public String name;
    public String label;
    public String description;
    private TableModel model;

    public Column(String name, String label, String description){
        this.name = name;
        this.label = label;
        this.description = description;
    }

    public void bindModel(TableModel model){
        this.model = model;
    }

    public Item bind(Object entity) throws Exception {
        String getterName = "get" + this.name;
        Method getter = entity.getClass().getMethod(getterName);
        String setterName = "set" + this.name;
        Method setter = entity.getClass().getMethod(setterName, String.class);
        Object result=null;
        result = getter.invoke(entity);
        ItemEditable item = new ItemEditable(this, result, setter, entity);
        return item;
    }

}
