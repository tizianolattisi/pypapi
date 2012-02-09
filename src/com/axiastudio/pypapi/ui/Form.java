/*
 * Copyright (C) 2011 AXIA Studio (http://www.axiastudio.com)
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
package com.axiastudio.pypapi.ui;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.trolltech.qt.gui.*;
import com.trolltech.qt.core.*;
import com.trolltech.qt.designer.QUiLoader;
import com.trolltech.qt.designer.QUiLoaderException;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.widgets.NavigationToolBar;
import com.axiastudio.pypapi.ui.widgets.PyPaPiEntityPicker;
import com.axiastudio.pypapi.ui.widgets.PyPaPiTableView;

/**
 *
 * @author tiziano
 */
public class Form extends QMainWindow implements IForm {

    private Class entityClass;
    private String uiFile;
    private String title;
    private Context context;
    private HashMap widgets;
    private List<Column> columns;
    private List<Column> entities;

    public Form(Form other) {
        this(other.uiFile, other.entityClass);
    }

    public Form(String uiFile, Class entityClass) {
        this(uiFile, entityClass, "");
    }

    public Form(String uiFile, Class entityClass, String title) {
        this.entityClass = entityClass;
        this.uiFile = uiFile;
        this.title = title;
        QFile file = Util.ui2jui(new QFile(uiFile));
        //QFile file = new QFile(uiFile);
        this.loadUi(file);
    }
    
    @Override
    public void init(){
        this.init(null);
    }
        
    @Override
    public void init(Store store){
        this.resolveColumns();
        /* root context */
        this.context = this.createContext(".", store);
        this.addMappers();
        /* context's children */
        this.initModels();
        NavigationToolBar bar = new NavigationToolBar("Navigation", this);
        bar.setMovable(true);
        this.addToolBar(bar);
        this.context.getMapper().currentIndexChanged.connect(this, "indexChanged(int)");
        this.context.getMapper().toFirst();
        bar.refresh();
    }

    private void loadUi(QFile uiFile){
        QWidget window = null;
        try {
            window = (QMainWindow) QUiLoader.load(uiFile);
        } catch (QUiLoaderException ex) {
            Logger.getLogger(Form.class.getName()).log(Level.SEVERE, null, ex);
        }
        for( QByteArray name: window.dynamicPropertyNames()){
            this.setProperty(name.toString(), window.property(name.toString()));
        }
        this.setCentralWidget(window);
    }

    private void initModels(){
        for (Object objColumn:this.entities){
            Column column = (Column) objColumn;
            this.createContext(column.getName());
        }

    }

    private Context createContext(String path){
        return this.createContext(path, null);
    }

    private Context createContext(String path, Store store){
        List contextColumns = null;
        Context dataContext = null;
        if(".".equals(path)){
            contextColumns = this.columns;
        } else {
            contextColumns = (List) Register.queryRelation(this.widgets.get(path), "columns");
        }
        if( store == null){
            dataContext = new Context(this, this.entityClass, path, contextColumns);
        } else {
            dataContext = new Context(this, this.entityClass, path, contextColumns, store);
        }
        Register.registerRelation(dataContext, this, path);
        if(! ".".equals(path)){
            ((QTableView) this.widgets.get(path)).setModel(dataContext.getModel());
            
        }
        return dataContext;
    }

    private void resolveColumns(){

        QObject child = null;
        List children = this.findChildren();
        Boolean isColumn;
        Boolean isEntity;
        List<Column> criteria;
        List<Column> searchColumns;

        criteria = new ArrayList();
        searchColumns = new ArrayList();
        this.columns = new ArrayList();
        this.entities = new ArrayList();
        this.widgets = new HashMap();

        for (int i=0; i<children.size(); i++){
            Object entityProperty=null;
            Column column=null;
            isColumn = false;
            isEntity = false;
            child = (QObject) children.get(i);
            Object columnProperty = child.property("column");
            if (columnProperty != null){
                isColumn = true;
            } else {
                entityProperty = child.property("entity");
                if (entityProperty != null){
                    isEntity = true;
                }
            }
            if (isColumn){
                String lookupPropertyName=null;
                String columnPropertyName = (String) columnProperty;
                Object lookupProperty = child.property("lookup");
                if ( lookupProperty != null){
                    lookupPropertyName = (String) lookupProperty;                    
                    lookupPropertyName = lookupPropertyName.substring(0,1).toUpperCase()
                            + lookupPropertyName.substring(1);
                }
                column = new Column(columnPropertyName, columnPropertyName, columnPropertyName,
                        lookupPropertyName);
                boolean add = this.columns.add(column);
                Object put = this.widgets.put(columnPropertyName, child);
            }
            if (isEntity){
                String entityPropertyName = (String) entityProperty;
                column = new Column(entityPropertyName, entityPropertyName, entityPropertyName);
                boolean add = this.entities.add(column);
                Object put = this.widgets.put(entityPropertyName, child);
            }

            // XXX: implements ILookable?
            if (child.getClass().equals(PyPaPiEntityPicker.class)){
                ((PyPaPiEntityPicker) child).setBindColumn(column);
            }
            // search dynamic property
            Object searchProperty = child.property("search");
            if (searchProperty != null){
                if ((Boolean) searchProperty){
                    criteria.add(column);
                }
            }
            // columns and reference for list value widget
            if (child.getClass().equals(PyPaPiTableView.class)){
                Object columnsProperty = child.property("columns");
                if (columnsProperty != null){
                    String[] columnNames = ((String) columnsProperty).split(",");
                    List<Column> tableColumns = new ArrayList();
                    for(String name: columnNames){
                        Column tableColumn = new Column(name, name, name);
                        tableColumns.add(tableColumn);
                    }
                    Register.registerRelation(tableColumns, child, "columns");
                }
                Object referenceProperty = child.property("reference");
                if (referenceProperty != null){
                    Register.registerRelation((String) referenceProperty, child, "reference");
                }
            }
        }
        // search columns
        Object searchColumnsProperty = this.property("searchcolumns");
        if (searchColumnsProperty != null){
            String[] columnNames = ((String) searchColumnsProperty).split(",");
            for(String name: columnNames){
                Column searchColumn = new Column(name, name, name);
                searchColumns.add(searchColumn);
            }
        }
        EntityBehavior behavior = new EntityBehavior(this.entityClass.getName());
        behavior.setCriteria(criteria);
        behavior.setSearchColumns(searchColumns);
        Register.registerUtility(behavior, IEntityBehavior.class, this.entityClass.getName());
    }

    private void addMappers() {
        for (int i=0; i<this.columns.size(); i++){
            Column column = (Column) this.columns.get(i);
            QObject widget = (QObject) this.widgets.get(column.getName());
            this.context.getMapper().addMapping((QWidget) widget, i);
        }
    }
    
    private void indexChanged(int row){
        int idx = this.context.getMapper().currentIndex() + 1;
        int tot = this.context.getModel().rowCount();
        this.setWindowTitle(this.title + " (" + idx + " of " + tot +")");
    }

    public Context getContext() {
        return context;
    }
    
}