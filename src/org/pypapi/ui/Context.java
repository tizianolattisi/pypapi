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

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

import org.pypapi.db.*;
import org.pypapi.GlobalManager;


/**
 *
 * @author tiziano
 */
public class Context extends QObject {

    public TableModel model;
    public QDataWidgetMapper mapper;
    public Class rootClass;
    public String name;
    public Object currentEntity;

    public Boolean atBof;
    public Boolean atEof;
    private Context primaryDc;

    public Context(QObject parent, Class rootClass, String name, List columns){
        Logger.getLogger(Context.class.getName()).log(Level.INFO, "Create {0} context", name);
        this.rootClass = rootClass;
        this.name = name;

        this.model = this.createModel();
        this.mapper = new QDataWidgetMapper(this);
        this.mapper.setSubmitPolicy(QDataWidgetMapper.SubmitPolicy.AutoSubmit);
        this.mapper.setModel(this.model);
        this.model.setColumns(columns);
        this.initializeContext();
    }

    private void initializeContext(){
        this.mapper.currentIndexChanged.connect(this, "indexChanged(int)");
        if(".".equals(this.name)){
            this.primaryDc = this;
        } else {
            this.primaryDc = (Context) GlobalManager.queryUtility(IContext.class, ".");
            this.primaryDc.mapper.currentIndexChanged.connect(this, "parentIndexChanged(int)");
        }
        this.model.dataChanged.connect(primaryDc, "modelDataChanged(QModelIndex, QModelIndex)");
        // XXX: implements inserting and removing rows 
        //this.model.rowsRemoved.connect(primaryDc, "modelDataChanged()");
        //this.model.rowsInserted.connect(primaryDc, "modelDataChanged()");
    }


    private TableModel createModel(){
        TableModel tableModel;

        /* resolve entity class */
        if(".".equals(this.name)){
            Database db = (Database) GlobalManager.queryUtility(IDatabase.class);
            // XXX: Database is not the right place fot a store factory...
            Store store = db.createStore(this.rootClass);
            tableModel = new TableModel(store, null);
        } else {
            tableModel = new TableModel(null, null);
        }

        return tableModel;
    }

    private void indexChanged(int row){
        int cnt = this.model.rowCount();
        if(".".equals(this.name)){
            if(cnt==0){
                this.atBof = true;
                this.atEof = true;
            } else {
                this.atBof = (row<1);
                this.atEof = (row>=cnt-1);
            }
        }
        if(cnt>0){
            this.currentEntity = this.model.getEntityByRow(row);
        }
    }

    private void parentIndexChanged(int row) throws Exception {
        /* retrieve this.name store */
        List result=null;
        String getterName = "get" + this.name.substring(1,2).toUpperCase() +
                this.name.substring(2);
        result = (List) this.primaryDc.currentEntity.getClass()
                 .getMethod(getterName).invoke(this.primaryDc.currentEntity);

        Store store = new Store(result);
        this.model.setStore(store);
        this.firstElement();
    }

    private void modelDataChanged(QModelIndex topLeft, QModelIndex bottomRight){
        // XXX: modelDataChanged
        System.out.println("modelDataChanged");
    }

    public void firstElement(){
        this.mapper.toFirst();
    }

    public void nextElement(){
        this.mapper.toNext();
    }

    public void previousElement(){
        this.mapper.toPrevious();
    }

    public void lastElement(){
        this.mapper.toLast();
    }

}
