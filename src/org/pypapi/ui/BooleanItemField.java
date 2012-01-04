/*
 * Copyright (C) 2012 tiziano
 * * Copyright (C) 2012 tiziano
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

import com.trolltech.qt.core.Qt;
import java.lang.reflect.Method;

/**
 *
 * @author tiziano
 */
public class BooleanItemField extends ItemField {
    
    public BooleanItemField(Column column, Object value, Method setterMethod,
            Object entity){
        super(column, value, setterMethod, entity);
    }
    
    public Qt.CheckState getCheckstate(){
        Boolean b = (Boolean) this.get();
        if( b == true){
            return Qt.CheckState.Checked;
        } else {
            return Qt.CheckState.Unchecked;
        }
    }
    
    public Boolean setCheckstate(Boolean objValue){
        return this.set(objValue);
    }
    
    @Override
    public Object getDisplay(){
        return null;
    }
}
