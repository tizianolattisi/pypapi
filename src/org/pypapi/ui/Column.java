/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pypapi.ui;

import java.lang.reflect.Method;


/**
 *
 * @author tiziano
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
