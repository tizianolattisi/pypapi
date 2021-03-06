/*
 * Copyright (C) 2012 AXIA Studio (http://www.axiastudio.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.pypapi.db;

import java.util.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Store<T> implements List {

    private List<T> list;

    public Store(List<T> list){
        if( list == null ){
            list = new ArrayList();
        }
        this.list = list;
        }

    @Override
    public int size(){
        return this.list.size();
    }

    @Override
    public Object get(int r){
        return this.list.get(r);
    }
    
/*    public void insert(int r, Object obj){
        this.list.add(r, obj);
    }*/

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.list.contains(o);
    }

    @Override
    public Iterator iterator() {
        return this.list.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.list.toArray();
    }

    @Override
    public Object[] toArray(Object[] ts) {
        return this.list.toArray(ts);
    }

    @Override
    public boolean add(Object e) {
        return this.list.add((T) e);
    }

    @Override
    public boolean remove(Object o) {
        return this.list.remove(o);
    }

    @Override
    public boolean containsAll(Collection clctn) {
        return this.list.containsAll(clctn);
    }

    @Override
    public boolean addAll(Collection clctn) {
        return this.list.addAll(clctn);
    }

    @Override
    public boolean addAll(int i, Collection clctn) {
        return this.list.addAll(i, clctn);
    }

    @Override
    public boolean removeAll(Collection clctn) {
        return this.list.removeAll(clctn);
    }

    @Override
    public boolean retainAll(Collection clctn) {
        return this.list.retainAll(clctn);
    }

    @Override
    public void clear() {
        this.list.clear();
    }

    @Override
    public Object set(int i, Object e) {
        return this.list.set(i, (T) e);
    }

    @Override
    public void add(int i, Object e) {
        this.list.add(i, (T) e);
    }

    @Override
    public Object remove(int i) {
        return this.list.remove(i);
    }

    @Override
    public int indexOf(Object o) {
        return this.list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.list.lastIndexOf(o);
    }

    @Override
    public ListIterator listIterator() {
        return this.list.listIterator();
    }

    @Override
    public ListIterator listIterator(int i) {
        return this.list.listIterator(i);
    }

    @Override
    public List subList(int i, int i1) {
        return this.list.subList(i, i1);
    }

    /*
     *  Order by toString()
     */
    public void sortByToString(){

        Collections.sort(this, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

    }
    
}
