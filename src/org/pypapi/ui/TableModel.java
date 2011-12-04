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

import com.trolltech.qt.core.Qt.ItemFlags;
import java.util.*;

import com.trolltech.qt.QVariant;
import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.pypapi.db.Store;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.it)
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
        if(qmi == null) return value;
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

    @Override
    public boolean setData(QModelIndex qmi, Object value, int role){
        ItemEditable item = null;
        if(qmi == null) return false;
        try {
            item = (ItemEditable) this.get(qmi.row(), (Column) this.columns.get(qmi.column()));
        } catch (Exception ex) {
            Logger.getLogger(TableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Object oldValue = item.getRoleValue(role);
            if (!oldValue.equals(value)){
                boolean res = item.setRoleValue(role, value);
            } else return true;
        } catch (Exception ex) {
            Logger.getLogger(TableModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        // clean cache
        this.purgeItemCache(this.store.get(qmi.row()), (Column) this.columns.get(qmi.column()));
        dataChanged.emit(qmi, qmi);
        return true;
    }

    @Override
    public Qt.ItemFlags flags(QModelIndex qmi){
        // XXX: flags coming out from Item
        ItemFlags flags = Qt.ItemFlag.createQFlags();
        flags.set(Qt.ItemFlag.ItemIsSelectable);
        flags.set(Qt.ItemFlag.ItemIsEnabled);
        flags.set(Qt.ItemFlag.ItemIsEditable);
        return flags;
    }

    private Object get(int row, Column column) throws Exception{
        Object entity = this.store.get(row);
        Object result = null;
        result = this.getByEntity(entity, column);
        return result;
    }

    public Object getByEntity(Object entity, Column column) throws Exception {
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
    
    public void purgeItemCache(Object entity){
        this.cache.put(entity, new HashMap());
    }

    public void purgeItemCache(Object entity, Column column){
        HashMap entityHM = (HashMap) this.cache.get(entity);
        entityHM.remove(column);
    }

    /*
     * replace the store with a store based on the passed entity list
     */
    public void replaceRows(List entities){
        this.setStore(new Store(entities));
    }

    /*
     * insert rows at the current position
     */
    public Boolean insertRows(int row, int count, QModelIndex parent, List entities){
        /*
        def insertRows(self, position, count, parent=QtCore.QModelIndex(), entities=None):

            if not self.resizable and not self._canCreate():
                return False
            if position <= -1:
                position = len(self.store)
            rows_indexes = range(position, position+count)
            self.beginInsertRows(parent, rows_indexes[0], rows_indexes[-1])
            self._purgeIndexCache()
            for idx,rown in enumerate(rows_indexes):
                if entities is not None:
                    new_item = entities[idx]
                else:
                    new_item = self.create()
                self.store.insert(position, new_item)
            self.endInsertRows()
            return True
         */
        return true;
    }
}


