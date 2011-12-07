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

/**
 *
 * @author AXIA Studio (http://www.axiastudio.it)
 */
public class Form extends QMainWindow implements IForm {

    public Class entityClass;
    public String uiFile;
    public String title;
    public Context context;
    public HashMap widgets;
    public List<Column> criteria;
    
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
        this.context.mapper.currentIndexChanged.connect(this, "indexChanged(int)");
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
        return this.createContext(path, null);
    }

    private Context createContext(String path, Store store){
        List contextColumns = null;
        Context dataContext = null;
        if(".".equals(path)){
            contextColumns = this.columns;
        } else {
            // TODO: columns from dynamic property
            contextColumns = new ArrayList();
            Column c = new Column("Description", "Description", "Description");
            contextColumns.add(c);
        }
        if( store == null){
            dataContext = new Context(this, this.entityClass, path, contextColumns);
        } else {
            dataContext = new Context(this, this.entityClass, path, contextColumns, store);
        }
        GlobalManager.registerRelation(dataContext, this, path);
        if(! ".".equals(path)){
            ((QTableView) this.widgets.get(path)).setModel(dataContext.model);
            
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

        this.columns = new ArrayList();
        this.entities = new ArrayList();
        this.criteria = new ArrayList();
        this.widgets = new HashMap();

        for (int i=0; i<children.size(); i++){
            child = (QObject) children.get(i);
            child.setProperty("parentForm", this);
            property = child.property("column");
            if (property != null){
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
            } else {
                property = child.property("entity");
                if (property != null){
                    propertyName = (String) property;
                    column = new Column(propertyName, propertyName, propertyName);
                    boolean add = this.entities.add(column);
                    Object put = this.widgets.put(propertyName, child);
                }
            }
            // XXX: implements ILookable?
            if (child.getClass().equals(PyPaPiEntityPicker.class)){
                ((PyPaPiEntityPicker) child).column = column;
            }
            // search dynamic property
            property = child.property("search");
            if (property != null){
                if ((Boolean) property){
                    this.criteria.add(column);
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
    
    private void indexChanged(int row){
        int idx = this.context.mapper.currentIndex() + 1;
        int tot = this.context.model.rowCount();
        this.setWindowTitle(this.title + " (" + idx + " of " + tot +")");
    }

}
