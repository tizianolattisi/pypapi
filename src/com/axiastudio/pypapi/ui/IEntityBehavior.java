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
package com.axiastudio.pypapi.ui;

import java.util.List;
import java.util.Set;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public interface IEntityBehavior {

    /**
     * @return the className
     */
    String getClassName();

    /**
     * @return the criteria
     */
    List<Column> getCriteria();

    /**
     * @param criteria the criteria to set
     */
    void setCriteria(List<Column> criteria);

    /**
     * @return the search columns list
     */
    List<Column> getSearchColumns();

    /**
     * @param searchColumns the search columns to set
     */
    void setSearchColumns(List<Column> searchColumns);
    
    /**
     * @param widgetName the name of the widget to validate
     * @param re the regolar expression
     */
    void setReValidator(String widgetName, String re);

    /**
     * @param widgetName the name of the widget
     */
    String getReValidator(String widgetName);

    /**
     * 
     */
    Set<String> getReValidatorKeys();

}
