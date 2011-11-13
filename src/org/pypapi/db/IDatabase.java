/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pypapi.db;


/**
 *
 * @author tiziano
 */
public interface IDatabase {
    void open();
    Store createStore(Class klass);
}
