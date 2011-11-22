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

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.trolltech.qt.gui.*;
import com.trolltech.qt.core.*;
import com.trolltech.qt.designer.QUiLoader;
import com.trolltech.qt.designer.QUiLoaderException;

import org.pypapi.GlobalManager;
import org.pypapi.ui.widgets.NavigationToolBar;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.it)
 */
public class Form extends QMainWindow {

    public Context context;
    public Class entityClass;

    public HashMap widgets;
    private List columns;
    private List entities;

    public Form(QFile uiFile, Class entityClass) {
        this.entityClass = entityClass;
        this.loadUi(uiFile);
        this.resolveColumns();
        /* root context */
        this.context = this.createContext(".");
        this.addMappers();
        /* context's children */
        this.initModels();
        NavigationToolBar bar = new NavigationToolBar("Navigation", this);
        bar.setMovable(false);
        this.addToolBar(bar);
        this.context.mapper.toFirst();
        bar.refresh();
    }

    private void loadUi(QFile uiFile){
        QWidget widget = null;
        try {
            widget = (QMainWindow) QUiLoader.load(uiFile);
        } catch (QUiLoaderException ex) {
            Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.layout().addWidget(widget);
    }

    private void initModels(){
        for (Object objColumn:this.entities){
            Column column = (Column) objColumn;
            this.createContext(column.name);
        }

    }

    private Context createContext(String path){
        List contextColumns = null;
        if(".".equals(path)){
            contextColumns = this.columns;
        } else {
            // per ora un set predefinito di colonne per le List
            contextColumns = new ArrayList();
            Column c = new Column("Description", "Description", "Description");
            contextColumns.add(c);
        }
        Context dataContext = new Context(this, this.entityClass, path, contextColumns);
        GlobalManager.registerUtility(dataContext, IContext.class, path);
        if(! ".".equals(path)){
            /* */
            ((QTableView) this.widgets.get(path)).setModel(dataContext.model);
        }
        return dataContext;
    }

    private void resolveColumns(){

        String propertyName = null;
        QObject child = null;
        Object property = null;
        Column column = null;
        List children = this.findChildren();

        this.columns = new ArrayList();
        this.entities = new ArrayList();
        this.widgets = new HashMap();

        for (int i=0; i<children.size(); i++){
            child = (QObject) children.get(i);
            property = child.property("column");
            if (property != null){
                propertyName = (String) property;
                column = new Column(propertyName, propertyName, propertyName);
                boolean add = this.columns.add(column);
                Object put = this.widgets.put(propertyName, child);
            } else {
                property = child.property("entity");
                if (property != null){
                    propertyName = (String) property;
                    column = new Column(propertyName, propertyName, propertyName);
                    boolean add = this.entities.add(column);
                    Object put = this.widgets.put(propertyName, child);
                }
            }
        }

    }

    private void addMappers() {
        for (int i=0; i<this.columns.size(); i++){
            Column column = (Column) this.columns.get(i);
            QObject widget = (QObject) this.widgets.get(column.name);
            this.context.mapper.addMapping((QWidget) widget, i);
        }
    }

}
