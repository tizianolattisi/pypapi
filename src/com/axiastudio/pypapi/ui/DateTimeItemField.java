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
import com.trolltech.qt.core.QTime;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class DateTimeItemField extends ItemField {
    
    static final int MINIMUMDATEYEAR = 1752;
    static final int MINIMUMDATEMONTH = 9;
    static final int MINIMUMDATEDAY = 14;
    
    public DateTimeItemField(Column column, Object value, Method setterMethod,
            Object entity){
        super(column, value, setterMethod, entity);
    }

    @Override
    public Object getDisplay() {
        return this.getEdit();
    }
    
    
    
    @Override
    public Object getEdit() {
        QDateTime dateTime;
        if( this.value == null ){
            dateTime = new QDateTime(new QDate(MINIMUMDATEYEAR, MINIMUMDATEMONTH, MINIMUMDATEDAY));
        } else {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime((Date) this.value);
            dateTime = new QDateTime(new QDate(gc.get(Calendar.YEAR),
                                               gc.get(Calendar.MONTH)+1,
                                               gc.get(Calendar.DATE)),
                                     new QTime(gc.get(Calendar.HOUR_OF_DAY),
                                               gc.get(Calendar.MINUTE),
                                               gc.get(Calendar.SECOND)));
        }
        return dateTime;
    }

    @Override
    public boolean setEdit(Object objValue) {
        QDateTime dateTime = (QDateTime) objValue;
        int year = dateTime.date().year();
        int month = dateTime.date().month();
        int day = dateTime.date().day();
        if( year==MINIMUMDATEYEAR && month==MINIMUMDATEMONTH && day==MINIMUMDATEDAY){
            return this.set(null);
        } else {
            GregorianCalendar gc = new GregorianCalendar(dateTime.date().year(),
                                                         dateTime.date().month()-1,
                                                         dateTime.date().day(),
                                                         dateTime.time().hour(),
                                                         dateTime.time().minute());
            return this.set(gc.getTime());
        }
    }
    
}
