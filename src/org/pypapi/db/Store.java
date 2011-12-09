/*
 * Copyright (C) 2011 AXIA Studio (http://www.axiastudio.it)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.pypapi.db;

import java.util.*;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.it)
 */
public class Store {

    private List list;

    public Store(List list){
        this.list = list;
    }

    public int size(){
        return this.list.size();
    }

    public Object get(int r){
        return this.list.get(r);
    }
    
    public void insert(int r, Object obj){
        this.list.add(r, obj);
    }

}
