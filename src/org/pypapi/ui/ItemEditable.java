/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pypapi.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiziano
 */
public class ItemEditable extends Item {
    
    private Method setter;
    private Object entity;
   
    public ItemEditable(Column column, Object value, Method setterMethod,
            Object entity){
        super(column, value);
        this.entity = entity;
        this.setter = setterMethod;
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
        Method m = this.getClass().getMethod(setterName, Object.class);
        result = m.invoke(this, objValue);
        return true;
    }

    public boolean set(Object objValue){
        // XXX: ItemEditable.set
        System.out.println("set "+objValue);
        try {
            Object res = this.setter.invoke(entity, objValue);
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

}
