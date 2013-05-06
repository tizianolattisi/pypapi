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

import java.util.Map;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public interface IController {

    Store createFullStore();

    Store createNewStore();
    
    //Store createStore();

    //Store createStore(int limit);
    
    Store createCriteriaStore(Map criteria);

    Store createCriteriaStore(Map criteria, Integer limit);
    
    Validation commit(Object entity);
    
    void delete(Object entity);

    Object refresh(Object entity);
    
    Object get(Long id);
    
}
