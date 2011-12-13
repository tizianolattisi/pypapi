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
 * @author AXIA Studio (http://www.axiastudio.it)
 */
public class Context extends QObject {

    private TableModel model;
    private QDataWidgetMapper mapper;
    private Class rootClass;
    private String name;
    private Object currentEntity;
    private Boolean isDirty;
    private Boolean atBof;
    private Boolean atEof;
    private Context primaryDc;
    private QWidget parent;

    public Context(QWidget parent, Class rootClass, String name, List columns){
        this(parent, rootClass, name, columns, null);
    }

    public Context(QWidget parent, Class rootClass, String name, List columns, Store store){
        Logger.getLogger(Context.class.getName()).log(Level.INFO, "Create {0} context", name);
        this.parent = parent;
        this.rootClass = rootClass;
        this.name = name;

        this.model = this.createModel(store);
        this.mapper = new QDataWidgetMapper(this);
        this.mapper.setSubmitPolicy(QDataWidgetMapper.SubmitPolicy.AutoSubmit);
        this.mapper.setModel(this.model);
        this.model.setColumns(columns);
        this.initializeContext();
        this.isDirty = false;
    }

    private void initializeContext(){
        this.mapper.currentIndexChanged.connect(this, "indexChanged(int)");
        if(".".equals(this.name)){
            this.primaryDc = this;
        } else {
            this.primaryDc = (Context) GlobalManager.queryRelation(this.parent, ".");
            this.primaryDc.mapper.currentIndexChanged.connect(this, "parentIndexChanged(int)");
        }
        this.model.dataChanged.connect(primaryDc, "modelDataChanged(QModelIndex, QModelIndex)");
        // TODO: implements inserting and removing rows 
        //this.model.rowsRemoved.connect(primaryDc, "modelDataChanged()");
        //this.model.rowsInserted.connect(primaryDc, "modelDataChanged()");
    }


    private TableModel createModel(Store store){
        TableModel tableModel;

        /* resolve entity class */
        if(".".equals(this.name)){
            Database db = (Database) GlobalManager.queryUtility(IDatabase.class);
            Controller controller = (Controller) GlobalManager.queryUtility(IController.class, this.rootClass.getName());
            if( store == null ){
                store = controller.createFullStore();
            }
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
        this.isDirty = true;
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

    public void insertElement(){
        Object entity = null;
        QModelIndex idx=null;
        String entityName = this.rootClass.getName();
        Class cls = (Class) GlobalManager.queryUtility(IFactory.class, entityName);
        try {
            entity = cls.newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(TableModel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        List entities = new ArrayList();
        boolean add = entities.add(entity);
        this.model.insertRows(this.model.rowCount(), 1, idx, entities);
        this.mapper.toLast();
        this.isDirty = true;
    }

    public void deleteElement(){

    }

    public void commitChanges(){
        Controller c = (Controller) GlobalManager.queryUtility(IController.class, this.primaryDc.currentEntity.getClass().getName());
        // TODO: create element from factory anche controller
        c.edit(this.primaryDc.currentEntity);
        this.isDirty = false;
    }

    public void cancelChanges(){
        Controller c = (Controller) GlobalManager.queryUtility(IController.class, this.primaryDc.currentEntity.getClass().getName());
        c.refresh(this.primaryDc.currentEntity);
        this.model.purgeItemCache(this.primaryDc.currentEntity);
        this.isDirty = false;
    }
    
    public void search(){
        Controller controller = (Controller) GlobalManager.queryUtility(IController.class, this.rootClass.getName());
        PickerDialog pd = new PickerDialog(this.parent, controller);
        int res = pd.exec();
        if ( res == 1 ){
            this.model.replaceRows(pd.getSelection());
            this.firstElement();
        }
    }

    public TableModel getModel() {
        return model;
    }

    public QDataWidgetMapper getMapper() {
        return mapper;
    }

    public Object getCurrentEntity() {
        return currentEntity;
    }

    public Boolean getAtBof() {
        return atBof;
    }

    public Boolean getAtEof() {
        return atEof;
    }

    public Boolean getIsDirty() {
        return isDirty;
    }

}
