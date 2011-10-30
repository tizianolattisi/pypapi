/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pypapi.db;

import org.hibernate.Session;

/**
 *
 * @author tiziano
 */
public interface IDatabase {
    void open();
    Session createNewSession();
}
