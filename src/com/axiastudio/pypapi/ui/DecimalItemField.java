/*
 * Copyright (C) 2013 AXIA Studio (http://www.axiastudio.com)
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
 * You should have received a copy of the GNU Afffero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.pypapi.ui;

import com.trolltech.qt.core.Qt;

import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class DecimalItemField extends ItemField {

    public DecimalItemField(Column column, Object value, Method setterMethod, Object entity) {
        super(column, value, setterMethod, entity);
    }
    
    @Override
    public Object getDisplay() {
        BigDecimal d = (BigDecimal) this.get();
        return d.floatValue();
    }

    @Override
    public Double getEdit(){
        /* The QDoubleSpinBox uses a Double value */
        BigDecimal d = (BigDecimal) this.get();
        return d.doubleValue();
    }
    
    @Override
    public boolean setEdit(Object objValue) {
        BigDecimal bd = new BigDecimal(objValue.toString());
        return this.set(bd);
    }
    
    @Override
    protected Qt.ItemFlags getFlags() {
        Qt.ItemFlags flags = Qt.ItemFlag.createQFlags();
        flags.set(Qt.ItemFlag.ItemIsSelectable);
        flags.set(Qt.ItemFlag.ItemIsEnabled);
        flags.set(Qt.ItemFlag.ItemIsEditable);
        return flags;
    }
}
