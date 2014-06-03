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

package com.axiastudio.pypapi.ui.widgets;

import com.axiastudio.pypapi.db.Store;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QWidget;

import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class PyPaPiComboBox extends QComboBox {
    
    private Store lookupStore;
    public static final String ND = "n.d.";

    public PyPaPiComboBox(QWidget qw) {
        super(qw);
        this.setEditable(false);
        this.setInsertPolicy(InsertPolicy.NoInsert);
    }

    public PyPaPiComboBox() {
        this(null);
    }

    public Store getLookupStore() {
        return lookupStore;
    }

    public void setLookupStore(Store lookupStore) {
        this.setLookupStore(lookupStore, Boolean.FALSE, Boolean.FALSE);
    }

    public void setLookupStore(Store lookupStore, Boolean notnull, Boolean sortByToString) {

        // sort by toString
        if( sortByToString ) {
            lookupStore.sortByToString();
        }

        this.clear();

        this.lookupStore = lookupStore;
        if( !notnull ){
            this.lookupStore.add(null);
        }
        for(int i=0; i<lookupStore.size(); i++){
            Object object = lookupStore.get(i);
            String key = ND;
            if( object != null ){
                key = object.toString();
            }
            this.addItem(key, object);
        }
        this.installEventFilter(this);
        this.editTextChanged.connect(this, "tryToSelect(String)");
        this.currentIndexChanged.connect(this, "tryToSelect(int)");
        this.activatedIndex.connect(this, "tryToSelect(int)");
    }

    public Object getCurrentEntity() {
        if( this.currentIndex() == -1 ){
            return null;
        }
        return this.lookupStore.get(this.currentIndex());
    }

    public String getCurrentText() {
        return this.itemText(this.currentIndex());
    }
    
    private void tryToSelect(int i){
        this.tryToSelect(null);
    }

    private void tryToSelect(String s){
        if( s == null || s.length() == 0 ){
            return;
        }
        Integer idx=null;
        Boolean gotcha=false;
        for(int i=0; i<this.lookupStore.size(); i++){
            Object object = this.lookupStore.get(i);
            String key = ND;
            if( object != null ){
                key = object.toString();
            }
            if( key.toLowerCase().contains(s.toLowerCase()) ){
                if( idx == null){
                    idx = i;
                    gotcha = true;
                } else {
                    gotcha = false;
                }
            }
        }
        if( gotcha ){
            this.setCurrentIndex(idx);
            this.setEditable(false);
            this.setFocus();
        } else {
            this.setEditable(true);
        }
    }

    /*
     *  Select the corresponding object in the store
     */
    public boolean select(Object object) {
        int i = this.lookupStore.indexOf(object);
        this.setCurrentIndex(i);
        this.setEditable(false);
        this.setFocus();
        return true;
    }
    
    @Override
    public boolean eventFilter(QObject qo, QEvent qevent) {
        if( this.isEnabled() && qevent.type() == QEvent.Type.MouseButtonPress ){
            this.setEditable(true);
            this.setEditText("");
            return true;
        }
        return super.eventFilter(qo, qevent);
    }
    
}
