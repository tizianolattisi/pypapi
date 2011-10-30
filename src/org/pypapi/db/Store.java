/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pypapi.db;

import java.util.*;

/**
 *
 * @author tiziano
 */
public class Store {

    public List list;

    public Store(List list){
        this.list = list;
    }

    public int size(){
        return this.list.size();
    }

    public Object get(int r){
        return this.list.get(r);
    }

}
