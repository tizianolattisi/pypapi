/*
 * Copyright (C) 2011 tiziano
 * * Copyright (C) 2011 tiziano
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

import java.util.List;
import org.pypapi.ui.Column;

/**
 *
 * @author tiziano
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
    
}
