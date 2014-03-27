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

import com.trolltech.qt.core.QDate;
import com.trolltech.qt.core.QDateTime;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class DateItemField extends ItemField {
    
    static final int MINIMUMDATEYEAR = 1752;
    static final int MINIMUMDATEMONTH = 9;
    static final int MINIMUMDATEDAY = 14;
    
    public DateItemField(Column column, Object value, Method setterMethod,
            Object entity){
        super(column, value, setterMethod, entity);
    }

    @Override
    public Object getDisplay() {
        return this.getEdit();
    }
    
    
    
    @Override
    public Object getEdit() {
        QDate date;
        if( this.value == null ){
            date = new QDate(MINIMUMDATEYEAR, MINIMUMDATEMONTH, MINIMUMDATEDAY);
        } else {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime((Date) this.value);
            date = new QDate(gc.get(Calendar.YEAR),
                    gc.get(Calendar.MONTH)+1,
                    gc.get(Calendar.DATE));
        }
        return date;
    }

    @Override
    public boolean setEdit(Object objValue) {
        QDate date = ((QDateTime) objValue).date();
        int year = date.year();
        int month = date.month();
        int day = date.day();
        if( year==MINIMUMDATEYEAR && month==MINIMUMDATEMONTH && day==MINIMUMDATEDAY){
            return this.set(null);
        } else {
            GregorianCalendar gc = new GregorianCalendar(date.year(),
                    date.month()-1, date.day());
            return this.set(gc.getTime());
        }
    }
    
}
