/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pypapi.ui;

import java.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.Criteria;

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
        //this.model.dataChanged.connect(primaryDc, "modelDataChanged(QModelIndex, QModelIndex)");
        //this.model.rowsRemoved.connect(primaryDc, "modelDataChanged()");
        //this.model.rowsInserted.connect(primaryDc, "modelDataChanged()");
    }


    private TableModel createModel(){
        TableModel tableModel;

        /* resolve entity class */
        if(".".equals(this.name)){
            /* in futuro dovrò risolvere uno store registrato */
            Database db = (Database) GlobalManager.queryUtility(IDatabase.class);
            Session session = db.createNewSession();
            Criteria crit = session.createCriteria(this.rootClass);
            List entities = crit.list();
            Store store = new Store(entities);
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

    /*
    private void modelDataChanged(QModelIndex topLeft, QModelIndex bottomRight){
        System.out.println("modelDataChanged");
        System.out.println(topLeft);
        System.out.println(bottomRight);
    }*/

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
