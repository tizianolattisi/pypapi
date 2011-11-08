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
public class ItemEditable extends Item {
   
    public ItemEditable(Column column, Object value){
        super(column, value);
    }

    public boolean setRoleValue(int role, Object objValue) throws Exception {
        /*
         * Set the value corresponding to the requested role
         * thru the correct setter method.
         */

        Object result;

        Object nameObject = ItemEditable.itemRolesNameMap.get(role);
        if( nameObject == null){
            return false;
        }
        String name = (String) nameObject;
        String setterName = "set" + name.substring(0,1).toUpperCase() +
                name.substring(1);
        System.out.println(setterName);
        Method m = this.getClass().getMethod(setterName, Object.class);
        result = m.invoke(this, objValue);
        return true;
    }

    public boolean set(Object objValue){
        
        return true;
    }

    public boolean setEdit(Object objValue){
        boolean res = this.set(objValue);
        return res;
    }

}
