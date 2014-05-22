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
package com.axiastudio.pypapi.ui;

import com.axiastudio.pypapi.db.Store;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.designer.QUiLoader;
import com.trolltech.qt.designer.QUiLoaderException;
import com.trolltech.qt.gui.*;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Dialog extends QDialog implements IForm {

    protected Class entityClass;
    protected String uiFile;
    //protected String title;
    private Context context;
    private HashMap<String, QObject> widgets;
    private List<Column> columns;
    private List<Column> entities;
    private IForm parentForm=null;

    
    public Dialog(String uiFile, Class entityClass) {
        this(uiFile, entityClass, "");
    }

    public Dialog(String uiFile, Class entityClass, String title) {
        this.entityClass = entityClass;
        this.uiFile = uiFile;
        //this.title = title;
        QFile file = Util.ui2jui(new QFile(uiFile));
        this.loadUi(file);
    }

    private void loadUi(QFile uiFile){
        QDialog dialog = null;
        try {
            dialog = (QDialog) QUiLoader.load(uiFile);
        } catch (QUiLoaderException ex) {
            Logger.getLogger(Dialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        for( QByteArray name: dialog.dynamicPropertyNames()){
            this.setProperty(name.toString(), dialog.property(name.toString()));
        }
        if( dialog.layout() == null ){
            Logger.getLogger(Dialog.class.getName()).log(Level.SEVERE, "Dialog UI should have a main layout");
        } else {
            QBoxLayout box=null;
            if(dialog.layout().getClass().equals(QVBoxLayout.class)){
                box = new QHBoxLayout();
            } else if(dialog.layout().getClass().equals(QHBoxLayout.class)){
                box = new QVBoxLayout();
            } else {
                // TODO: QGridLayout?
                Logger.getLogger(Dialog.class.getName()).log(Level.WARNING, "Dialog UI can not have a QGridLayout");
            }
            if( box != null ){
                box.addStretch();
                //QToolButton buttonCancel = new QToolButton(this);
                //buttonCancel.setIcon(new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/cancel.png"));
                //buttonCancel.clicked.connect(this, "reject()");
                //box.addWidget(buttonCancel);
                QToolButton buttonAccept = new QToolButton();
                buttonAccept.setIcon(new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/accept.png"));
                buttonAccept.clicked.connect(this, "accept()");
                box.addWidget(buttonAccept);
                dialog.layout().addItem(box);
            }
            this.setLayout(dialog.layout());
            this.setModal(true);
        }
        this.setWindowTitle(dialog.windowTitle());
    }

    @Override
    public void init() {
        this.init(null);
    }

    @Override
    public void init(Store store) {
        FormConfigurator configurator = new FormConfigurator(this, this.entityClass);
        configurator.configure(store);
        this.getContext().getMapper().toFirst();
        storeInitialized.emit();
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    @Override
    public void setEntities(List<Column> entities) {
        this.entities = entities;
    }

    @Override
    public void setWidgets(HashMap<String, QObject> widgets) {
        this.widgets = widgets;
    }

    @Override
    public List<Column> getColumns() {
        return columns;
    }

    @Override
    public Column getColumn(String columnName) {
        for( Column column: getColumns() ){
            if( column.getName().equals(columnName) ){
                return column;
            }
        }
        return null;
    }

    @Override
    public List<Column> getEntities() {
        return entities;
    }

    @Override
    public HashMap<String, QObject> getWidgets() {
        return widgets;
    }

    @Override
    public IForm getParentForm() {
        return parentForm;
    }

    @Override
    public void setParentForm(IForm parentForm) {
        this.parentForm = parentForm;
    }

    @Override
    protected void showEvent(QShowEvent arg__1) {
        super.showEvent(arg__1);
        formShown.emit();
    }

    /* SIGNALS */
    
    public Signal0 storeInitialized = new Signal0();
    public Signal0 formShown = new Signal0();

    
}
