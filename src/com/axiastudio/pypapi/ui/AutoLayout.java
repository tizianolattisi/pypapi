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

import com.trolltech.qt.gui.*;

import java.lang.reflect.Field;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class AutoLayout {
    
    public static QMainWindow createWindowLayout(Class entityClass){
        QMainWindow win = new QMainWindow();
        QWidget centralWidget = new QWidget(win);
        QGridLayout gridLayout = new QGridLayout();
        centralWidget.setLayout(gridLayout);
        win.setCentralWidget(centralWidget);
        Field[] declaredFields = entityClass.getDeclaredFields();
        for(int i=0; i<declaredFields.length; i++){
            Field field = declaredFields[i];
            if( !field.getName().equals("serialVersionUID") ){
                QWidget widget=null;
                if( field.getType() == String.class ){
                    widget = new QLineEdit();
                } else if( field.getType().isEnum() ){
                    widget = new QComboBox();
                    for( Object object:  field.getType().getEnumConstants() ){
                        String key = object.toString();
                        ((QComboBox) widget).addItem(key, object);
                    } 
                }
                if( widget != null ){
                    gridLayout.addWidget(new QLabel(field.getName()), i, 0);
                    widget.setProperty("column", field.getName());
                    gridLayout.addWidget(widget, i, 1);
                }
            }
        }
        return win;
    }
    
}
