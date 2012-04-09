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

import java.lang.reflect.Method;
import java.util.List;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class ChoiceItemField extends ItemField {
    
    List choices;

    public ChoiceItemField(Column column, Object value, Method setterMethod,
            Object entity, List choices) {
        super(column, value, setterMethod, entity);
        this.choices = choices;
    }

    @Override
    public Object getEdit() {
        Object editValue = super.getEdit();
        for( int i=0; i<this.choices.size(); i++ ){
            Object choice = this.choices.get(i);
            if( choice.equals(editValue) ){
                return i;
            }
        }
        return null;
    }

    @Override
    public boolean setEdit(Object objValue) {
        Integer index = (Integer) objValue;
        return super.setEdit(this.choices.get(index));
    }
    
}
