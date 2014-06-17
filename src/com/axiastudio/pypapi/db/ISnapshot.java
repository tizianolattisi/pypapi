package com.axiastudio.pypapi.db;

/**
 * User: tiziano
 * Date: 17/06/14
 * Time: 15:44
 */
public interface ISnapshot<T> {

    public void takeSnapshot();
    public T getSnapshot();

}
