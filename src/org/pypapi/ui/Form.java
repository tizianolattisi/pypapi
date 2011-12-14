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
import org.pypapi.db.Store;
import org.pypapi.ui.widgets.NavigationToolBar;
import org.pypapi.ui.widgets.PyPaPiEntityPicker;
import org.pypapi.ui.widgets.PyPaPiTableView;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.it)
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
        bar.setMovable(false);
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
        this.layout().addWidget(window);
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
            contextColumns = (List) GlobalManager.queryRelation(this.widgets.get(path), "columns");
        }
        if( store == null){
            dataContext = new Context(this, this.entityClass, path, contextColumns);
        } else {
            dataContext = new Context(this, this.entityClass, path, contextColumns, store);
        }
        GlobalManager.registerRelation(dataContext, this, path);
        if(! ".".equals(path)){
            ((QTableView) this.widgets.get(path)).setModel(dataContext.getModel());
            
        }
        return dataContext;
    }

    private void resolveColumns(){

        QObject child = null;
        Object property = null;
        String propertyName = null;
        Object lookupProperty = null;
        String lookupPropertyName = null;
        Column column = null;
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
            isColumn = false;
            isEntity = false;
            child = (QObject) children.get(i);
            property = child.property("column");
            if (property != null){
                isColumn = true;
            } else {
                property = child.property("entity");
                if (property != null){
                    isEntity = true;
                }
            }
            if (isColumn){
                propertyName = (String) property;
                lookupProperty = child.property("lookup");
                if ( lookupProperty != null){
                    lookupPropertyName = (String) lookupProperty;                    
                    lookupPropertyName = lookupPropertyName.substring(0,1).toUpperCase()
                            + lookupPropertyName.substring(1);
                }
                column = new Column(propertyName, propertyName, propertyName,
                        lookupPropertyName);
                boolean add = this.columns.add(column);
                Object put = this.widgets.put(propertyName, child);
            }
            if (isEntity){
                propertyName = (String) property;
                column = new Column(propertyName, propertyName, propertyName);
                boolean add = this.entities.add(column);
                Object put = this.widgets.put(propertyName, child);
            }

            // XXX: implements ILookable?
            if (child.getClass().equals(PyPaPiEntityPicker.class)){
                ((PyPaPiEntityPicker) child).setColumn(column);
            }
            // search dynamic property
            property = child.property("search");
            if (property != null){
                if ((Boolean) property){
                    criteria.add(column);
                }
            }
            // columns for list value widget
            if (child.getClass().equals(PyPaPiTableView.class)){
                property = child.property("columns");
                if (property != null){
                    String[] columnNames = ((String) property).split(",");
                    List<Column> tableColumns = new ArrayList();
                    for(String name: columnNames){
                        Column tableColumn = new Column(name, name, name);
                        tableColumns.add(tableColumn);
                    }
                    GlobalManager.registerRelation(tableColumns, child, "columns");
                }
            }
        }
        // search columns
        property = this.property("searchcolumns");
        if (property != null){
            String[] columnNames = ((String) property).split(",");
            for(String name: columnNames){
                Column searchColumn = new Column(name, name, name);
                searchColumns.add(searchColumn);
            }
        }
        EntityBehavior behavior = new EntityBehavior(this.entityClass.getName());
        behavior.setCriteria(criteria);
        behavior.setSearchColumns(searchColumns);
        GlobalManager.registerUtility(behavior, IEntityBehavior.class, this.entityClass.getName());
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
