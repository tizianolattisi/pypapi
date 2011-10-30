/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pypapi.ui;


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
        result = entity.getClass().getMethod(getterName).invoke(entity);
        Item item = new Item(this, result);
        return item;
    }

}
