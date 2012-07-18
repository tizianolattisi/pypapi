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

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.Resolver;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.IController;
import com.axiastudio.pypapi.db.IStoreFactory;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.widgets.PyPaPiComboBox;
import com.axiastudio.pypapi.ui.widgets.PyPaPiEntityPicker;
import com.axiastudio.pypapi.ui.widgets.PyPaPiTableView;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QRegExp;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class FormConfigurator {

    private IForm form;
    private Class entityClass;

    public FormConfigurator(IForm form, Class entityClass) {
        this.form = form;
        this.entityClass = entityClass;
    }
    
    public void configure(){
        this.configure(null);
    }

    public void configure(Store store){
        this.resolveColumns();
        this.addValidators();
        Context context = this.createContext(".", store);
        this.form.setContext(context);
        this.addMappers();
        this.initModels();
    }
    
    private void initModels(){
        for (Object objColumn:this.form.getEntities()){
            Column column = (Column) objColumn;
            this.createContext(column.getName());
        }

    }
    private void resolveColumns(){

        QObject child;
        List children = this.form.findChildren();
        Boolean isColumn;
        Boolean isEntity;

        List<Column> columns = new ArrayList();
        List<Column> entities = new ArrayList();
        HashMap<String, QObject> widgets = new HashMap();

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
                String columnPropertyName = this.capitalize((String) columnProperty);
                Object lookupProperty = child.property("lookup");
                if ( lookupProperty != null){
                    lookupPropertyName = this.capitalize((String) lookupProperty);
                }
                column = new Column(columnPropertyName, columnPropertyName, columnPropertyName,
                        lookupPropertyName);
                boolean add = columns.add(column);
                Object put = widgets.put(columnPropertyName, child);
            }
            if (isEntity){
                String entityPropertyName = this.capitalize((String) entityProperty);
                column = new Column(entityPropertyName, entityPropertyName, entityPropertyName);
                boolean add = entities.add(column);
                Object put = widgets.put(entityPropertyName, child);
            }

            // XXX: implements ILookable?
            if (child.getClass().equals(PyPaPiEntityPicker.class)){
                ((PyPaPiEntityPicker) child).setBindColumn(column);
            }
            if (child.getClass().equals(PyPaPiComboBox.class)){
                Method storeFactory = (Method) Register.queryUtility(IStoreFactory.class, column.getName());
                Store lookupStore=null;
                if( storeFactory != null ){
                    try {
                        lookupStore = (Store) storeFactory.invoke(this.form);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(FormConfigurator.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        Logger.getLogger(FormConfigurator.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InvocationTargetException ex) {
                        Logger.getLogger(FormConfigurator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    Class entityClassFromReference = Resolver.entityClassFromReference(this.entityClass, column.getName());
                    Controller controller = (Controller) Register.queryUtility(IController.class, entityClassFromReference.getName());
                    lookupStore = controller.createFullStore();
                    column.setLookupStore(lookupStore);
                }
                ((PyPaPiComboBox) child).setLookupStore(lookupStore);
            }
            // columns and reference for list value widget
            if (child.getClass().equals(PyPaPiTableView.class)){
                Object headersProperty = child.property("headers");
                String[] headerNames=null;
                if (headersProperty != null){
                    headerNames = ((String) headersProperty).split(",");
                }
                Object columnsProperty = child.property("columns");
                if (columnsProperty != null){
                    String[] columnNames = ((String) columnsProperty).split(",");
                    List<Column> tableColumns = new ArrayList();
                    for(int c=0; c<columnNames.length; c++){
                        QHeaderView.ResizeMode resizeMode = Util.extractResizeMode(columnNames[c]);
                        String name = Util.cleanColumnName(columnNames[c]);
                        name = this.capitalize(name);
                        String label = name;
                        if( headerNames != null){
                            label = headerNames[c];
                        }
                        Column tableColumn = new Column(name, label, name, null, resizeMode.value());
                        tableColumns.add(tableColumn);
                    }
                    Register.registerRelation(tableColumns, child, "columns");
                }
                Object referenceProperty = child.property("reference");
                if (referenceProperty != null){
                    Register.registerRelation((String) referenceProperty, child, "reference");
                }
            }
            // Delegate
            if (child.getClass().equals(PyPaPiTableView.class)){
                PyPaPiTableView ptv = (PyPaPiTableView) child;
                ptv.setItemDelegate(new Delegate(ptv));
            }
        }
        this.form.setColumns(columns);
        this.form.setEntities(entities);
        this.form.setWidgets(widgets);
    }    
    
    private Context createContext(String path){
        return this.createContext(path, null);
    }

    private Context createContext(String path, Store store){
        List contextColumns;
        Context dataContext;
        if(".".equals(path)){
            contextColumns = this.form.getColumns();
        } else {
            contextColumns = (List) Register.queryRelation(this.form.getWidgets().get(path), "columns");
        }
        if( store == null){
            dataContext = new Context(this.form, this.entityClass, path, contextColumns);
        } else {
            dataContext = new Context(this.form, this.entityClass, path, contextColumns, store);
        }
        Register.registerRelation(dataContext, this.form, path);
        if(! ".".equals(path)){
            QTableView qtv = (QTableView) this.form.getWidgets().get(path);
            qtv.setModel(dataContext.getModel());
            this.setResizeModes(qtv);
        }
        return dataContext;
    }
    
    private void setResizeModes(QTableView qtv){
        TableModel model = (TableModel) qtv.model();
        QHeaderView horizontalHeader = qtv.horizontalHeader();
        for( int i=0; i<model.getColumns().size(); i++ ){
            Column c = model.getColumns().get(i);
            QHeaderView.ResizeMode mode = QHeaderView.ResizeMode.resolve(c.getResizeModeValue());
            horizontalHeader.setResizeMode(i, mode);
        }
    }
    
    private void addMappers() {
        for (int i=0; i<this.form.getColumns().size(); i++){
            Column column = (Column) this.form.getColumns().get(i);
            QObject widget = (QObject) this.form.getWidgets().get(column.getName());
            if( widget.getClass().equals(QTextEdit.class)){
                this.form.getContext().getMapper().addMapping((QTextEdit) widget, i, new QByteArray("plainText"));
                ((QTextEdit) widget).setTabChangesFocus(true);
            } else if( widget.getClass().equals(QCheckBox.class) ){
                this.form.getContext().getMapper().addMapping((QCheckBox) widget, i, new QByteArray("checked"));
                ((QCheckBox) widget).clicked.connect(this.form.getContext().getMapper(), "submit()", Qt.ConnectionType.AutoConnection);
            } else if( widget.getClass().equals(QComboBox.class) ){
                this.form.getContext().getMapper().addMapping((QComboBox) widget, i, new QByteArray("currentIndex"));
            } else if( widget.getClass().equals(PyPaPiComboBox.class) ){
                this.form.getContext().getMapper().addMapping((PyPaPiComboBox) widget, i, new QByteArray("currentIndex"));
            } else {
                this.form.getContext().getMapper().addMapping((QWidget) widget, i);
            }
        }
    }
    
    private void addValidators() {
        EntityBehavior behavior = (EntityBehavior) Register.queryUtility(IEntityBehavior.class, this.entityClass.getName());
        for( String widgetName: behavior.getReValidatorKeys() ){
            QObject widget = this.form.getWidgets().get(widgetName);
            if( widget.getClass() == QLineEdit.class ){
                String re = behavior.getReValidator(widgetName);
                QRegExp regExp = new QRegExp(re);
                QRegExpValidator validator = new QRegExpValidator(widget);
                validator.setRegExp(regExp);
                ((QLineEdit) widget).setValidator(validator);
            }
        }
    }
    
    private String capitalize(String s) {
        return s.substring(0,1).toUpperCase() + s.substring(1);
    }
}
