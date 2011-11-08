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
        Object result=null;
        Method m = entity.getClass().getMethod(getterName);
        result = m.invoke(entity);
        ItemEditable item = new ItemEditable(this, result);
        return item;
    }

}
