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
package org.pypapi.ui;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.it)
 * 
 * The EntityBehavior class holds the dynamic properties values, retrieved from the
 * form's design. Thru these properties the framework can determine the
 * behaviors of the object, such the selection's path, the lookup's column, and
 * the search criteria.
 */
public class EntityBehavior implements IEntityBehavior {
    
    private String className;
    private List<Column> criteria;
    private List<Column> searchColumns;
    
    public EntityBehavior(String className){
        this.className = className;
    }

    /**
     * @return the className
     */
    @Override
    public String getClassName() {
        return className;
    }

    /**
     * @return the criteria
     */
    @Override
    public List<Column> getCriteria() {
        return criteria;
    }

    /**
     * @param criteria the criteria to set
     */
    @Override
    public void setCriteria(List<Column> criteria) {
        this.criteria = criteria;
    }

    /**
     * @return the searchColumns
     */
    @Override
    public List<Column> getSearchColumns() {
        return searchColumns;
    }

    /**
     * @param searchColumns the searchColumns to set
     */
    @Override
    public void setSearchColumns(List<Column> searchColumns) {
        this.searchColumns = searchColumns;
    }
    
}
