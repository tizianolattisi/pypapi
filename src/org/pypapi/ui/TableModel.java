/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.pypapi.ui;

import java.util.*;

import com.trolltech.qt.QVariant;
import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.pypapi.db.Store;

/**
 *
 * @author tiziano
 */
public class TableModel extends QAbstractTableModel {

    public List columns;

    private Store store;
    private HashMap cache;

    public TableModel(Store store, List columns){
        if( store == null ) store = new Store(new ArrayList());
        this.store = store;
        if (columns != null) this.setColumns(columns);
        this.cache = new HashMap();
    }

    public void setStore(Store store){
        this.beginResetModel();
        this.store = store;
        this.cache = new HashMap();
        this.endResetModel();
    }
    public Store getStore(){
        return this.store;
    }

    private List getColumns(){
        return this.columns;
    }

    public final void setColumns(List columns){
        for( int i=0; i<columns.size(); i++){
            Column column = (Column) columns.get(i);
            column.bindModel(this);
        }
        this.columns = columns;
    }

    @Override
    public QModelIndex index(int row, int column, QModelIndex parent){
        assert parent == null;
        if (row<0 || row>this.store.size()-1){
            QModelIndex qmi = null;
            return qmi;
        }
        QModelIndex qmi = this.createIndex(row, column);
        return qmi;
    }

    @Override
    public int rowCount(QModelIndex qmi) {
        return this.store.size();
    }

    @Override
    public int columnCount(QModelIndex qmi) {
        return this.columns.size();
    }

    @Override
    public Object data(QModelIndex qmi, int role) {

        Object value = new QVariant();
        Item item = null;
        try {
            item = (Item) this.get(qmi.row(), (Column) this.columns.get(qmi.column()));
        } catch (Exception ex) {
            Logger.getLogger(TableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            value = item.getRoleValue(role);
        } catch (Exception ex) {
            Logger.getLogger(TableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return value;

    }

    private Object get(int row, Column column) throws Exception{
        Object entity = this.store.get(row);
        Object result = null;
        result = this.getByEntity(entity, column);
        return result;
    }

    private Object getByEntity(Object entity, Column column) throws Exception {
        Item result=null;
        HashMap hm=null;
        if (this.cache.containsKey(entity)) {
            hm = (HashMap) this.cache.get(entity);
            if (hm.containsKey(column)){
                result = (Item) hm.get(column);
            } else {
                result = column.bind(entity);
                hm.put(column, result);
            }
        } else {
            result = column.bind(entity);
            hm = new HashMap();
            hm.put(column, result);
            this.cache.put(entity, hm);
        }
        return result;
    }

    public Object getEntityByRow(int row){
        return this.store.get(row);
    }

}


