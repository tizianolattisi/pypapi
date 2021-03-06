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
import com.axiastudio.pypapi.db.IFactory;
import com.axiastudio.pypapi.db.Store;
import com.axiastudio.pypapi.ui.widgets.PyPaPiTableView;
import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.core.QTemporaryFile;
import com.trolltech.qt.gui.*;
import com.trolltech.qt.gui.QHeaderView.ResizeMode;
import com.trolltech.qt.gui.QMessageBox.StandardButton;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Util {
    
    public static Window formFromName(String formName){
        Window form=null;
        Class<? extends Window> formClass = (Class) Register.queryUtility(IForm.class, formName);
        String uiFile = (String) Register.queryUtility(IUIFile.class, formName);
        Class factory = (Class) Register.queryUtility(IFactory.class, formName);
        try {
            Constructor<? extends Window> constructor = formClass.getConstructor(new Class[]{String.class, Class.class, String.class});
            try {
                form = constructor.newInstance(new Object[]{uiFile, factory, ""});
            } catch (InstantiationException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        form.init(); // XXX: full store
        return form;
    }
    
    public static IForm formFromStore(Store store){
        IForm form=null;
        Object entity = store.get(0);
        String name = entity.getClass().getName();
        Class<? extends IForm> formClass = (Class) Register.queryUtility(IForm.class, entity.getClass().getName());
        if (formClass == null ){
            return null;
        }
        String uiFile = (String) Register.queryUtility(IUIFile.class, name);
        Class factory = (Class) Register.queryUtility(IFactory.class, name);
        try {
            Constructor<? extends IForm> constructor = formClass.getConstructor(new Class[]{String.class, Class.class, String.class});
            try {
                form = constructor.newInstance(new Object[]{uiFile, factory, ""});
            } catch (InstantiationException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        if( form != null ){
            form.init(store);
            return form;
        } else {
            return null;
        }
    }
    
    public static IForm formFromEntity(Object entity){
        List entities = new ArrayList();
        entities.add(entity);
        Store store = new Store(entities);
        return Util.formFromStore(store);
    }
    
    /*
     * Extract a named column from a list of columns
     */
    public static Column columnFromName(Collection<Column> columns, String name){
        for(Column column: columns){
            if( column.getName().equals(name) ){
                return column;
            }
        }
        return null;
    }
    
    public static QFile ui2jui(QFile ui){
        /*
         * Convert a ui QFile in a QFile jui.
         * The jui file is a ui file without the <?xml?> tag, and with the language
         * property set to jambi.
         * Something like:
         * sed 's/<ui version="4.0">/<ui version="4.0" language="jambi">/g' $1 | tail -n +2
         */
        String content;
        QTemporaryFile jui;
        long toRead=ui.size();
        String uiTag = "<ui version=\"4.0\" language=\"jambi\">";
        ui.open(new QFile.OpenMode(QFile.OpenModeFlag.ReadOnly,
                                   QFile.OpenModeFlag.Unbuffered));
        toRead -= ui.readLine().size();
        toRead -= ui.readLine().size();
        content = uiTag + "\n" + ui.read(toRead).toString();
        jui = new QTemporaryFile();
        jui.open(new QFile.OpenMode(QFile.OpenModeFlag.WriteOnly,
                                    QFile.OpenModeFlag.Unbuffered));
        jui.write(new QByteArray(content));
        jui.close();
        return jui;
    }
    
    public static void warningBox(QWidget parent, String title, String description){
        StandardButton res = QMessageBox.warning(parent, title, description);
    }

    public static void errorBox(QWidget parent, String title, String description){
        StandardButton res = QMessageBox.critical(parent, title, description);
    }

    public static void informationBox(QWidget parent, String title, String description){
        StandardButton res = QMessageBox.information(parent, title, description);
    }
    
    public static Boolean questionBox(QWidget parent, String title, String description){
        int res = QMessageBox.question(parent, title, description, 
                QMessageBox.StandardButton.Yes, QMessageBox.StandardButton.No);
        return res == StandardButton.Yes.value();
    }
    
    /*
     * Clean the column name, removing the ResizeMode marker.
     */
    public static String cleanColumnName(String name){
        String first = name.substring(0, 1);
        String last = name.substring(name.length()-1,name.length());
        if( "<".equals(first) || ">".equals(first) ){
            name = name.substring(1);
        }
        if( "<".equals(last) || ">".equals(last) ){
            name = name.substring(0,name.length()-1);
        }        
        return name;
    }
    
    /*
     *  Extract the ResizeMode from the column's name
     */
    public static ResizeMode extractResizeMode(String name){
        String first = name.substring(0, 1);
        String last = name.substring(name.length()-1,name.length());
        if( "<".equals(first) && ">".equals(last) ){
            return QHeaderView.ResizeMode.Stretch;
        } else if( ">".equals(first) && "<".equals(last) ){
            return QHeaderView.ResizeMode.ResizeToContents;
        }
        return QHeaderView.ResizeMode.Interactive;
    }
    
    /*
     * Find the first parent of type QMainWindow
     */
    public static QWidget findParentForm(QWidget widget){
        QWidget out=widget;
        while(!IForm.class.isInstance(out)){
            out = out.parentWidget();
        }
        return out;
    }

    /*
     * Find the first parent of type QMainWindow
     */
    public static QMdiArea findParentMdiArea(QWidget widget){
        QWidget out=widget;
        while(out != null && !QMdiArea.class.isInstance(out)){
            out = out.parentWidget();
        }
        if( out != null){
            return (QMdiArea) out;
        }
        return null;
    }
    
    /*
     * Export a list of entities in a csv content
     */
    public static String exportToCvs(List entities, List<Column> columns, Class entityClass){
        List<Method> methods = new ArrayList();
        String out = "";
        for( Column column: columns ){
            Method method = Resolver.getterFromFieldName(entityClass, column.getName());
            methods.add(method);
            // Headers
            out += "\"" + column.getLabel() + "\",";
        }
        if( methods.size() == 0 ){
            return null;
        }
        for( Object entity: entities ){
            String row = "";
            for( Method method: methods ){
                Object value=null;
                Class<?> returnType = method.getReturnType();
                try {
                    value = method.invoke(entity);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (value == null ) {
                    if  (String.class == returnType || returnType.isEnum() || Date.class == returnType ||
                                    Serializable.class.isAssignableFrom(returnType)) {
                        row += ",\"\"";
                    } else {
                        row += ",";
                    }
                } else {
                    if( String.class == returnType ){
                        row += ",\"" + value.toString().replaceAll("\"", "\"\"") + "\"";
                    } else if( Boolean.class == returnType ) {
                        Boolean b = (Boolean) value;
                        row += "," + b.toString().toUpperCase();
                    } else if( returnType.isEnum() ) {
                        row += ",\"" + value.toString() + "\"";
                    } else if( Date.class == returnType ) {
                        SimpleDateFormat sdf = new SimpleDateFormat();
                        sdf.applyPattern("yyyy-MM-dd");
                        row += "," + sdf.format(value);
                    } else if( Serializable.class.isAssignableFrom(returnType) ) {
                        row += ",\"" + value.toString() + "\"";
                    } else {
                        row += "," + value.toString();
                    }
                }
            }
            out += "\n" + row.substring(1);
        }
        return out;
    }
    
    public static void setWidgetReadOnly(QWidget widget, Boolean readOnly){
        if( widget instanceof QTextEdit ){
            ((QTextEdit) widget).setReadOnly(readOnly);
        } else if( widget instanceof QLineEdit ){
            ((QLineEdit) widget).setReadOnly(readOnly);
        } else if( widget instanceof QComboBox || widget instanceof QAbstractButton || widget instanceof QCheckBox ||
                   widget instanceof QDateEdit || widget instanceof QDateTimeEdit ||
                   widget instanceof QSpinBox || widget instanceof QDoubleSpinBox || widget instanceof QGroupBox){
            widget.setEnabled(!readOnly);
        } else if( widget instanceof PyPaPiTableView ){
            ((PyPaPiTableView) widget).setReadOnly(readOnly);
            QAbstractItemModel model = ((PyPaPiTableView) widget).model();
            if( model != null ){
                if( model instanceof ProxyModel ){
                    model = ((ProxyModel) model).sourceModel();
                }
                if( model instanceof TableModel ){
                    ((TableModel) model).setEditable(false);
                }
                ((ITableModel) model).setEditable(!readOnly);
            }
        }

    }

    public static List filterList(List initList, Map criteria, Class klass){

        List listResult = new ArrayList();

        for( Object k: criteria.keySet() ){
            Column column = (Column) k;
            if( !column.getName().contains(".") ){
                Method method = Resolver.getterFromFieldName(klass, column.getName());
                Class objClass = method.getReturnType();
                String op = column.getLabel();
                if ( op == null ) {
                    System.out.println("Operatore mancante!");
                    continue;
                }
                for ( Object obj: initList ) {
                    Object objValue = Resolver.valueFromFieldName(klass.cast(obj), column.getName());
                    if ( op.equals("is null") ) {
                        if ( objValue == null ){
                            listResult.add(obj);
                        }
                    } else if ( op.equals("is not null") ) {
                        if ( objValue != null ){
                            listResult.add(obj);
                        }
                    } else {
                        int compResult=Integer.MIN_VALUE;

                        if ( objClass.equals(Date.class) ) {
                            Date value = (Date) objValue;
                            Date condition = new Date();
                            String strCondition = (String) criteria.get(column);
                            if ( strCondition.trim().length()==10 ) {
                                strCondition += " 00:00";
                            }
                            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ITALIAN);
                            try {
                                condition = df.parse(strCondition);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            compResult = value.compareTo(condition);
                        } else if ( objClass.equals(Long.class) || objClass.equals(Integer.class) ) {
                            Long value = (Long) objValue;
                            Long condition = Long.parseLong((String) criteria.get(column));
                            compResult = value.compareTo(condition);
                        } else if ( objClass.equals(String.class) ) {
                            String value = (String) objValue;
                            compResult = value.compareTo((String) criteria.get(column));
                        }

                        if ( compResult==Integer.MIN_VALUE ) {
                            System.out.println("Errore nel compareTo()????");
                        }

                        if (op.equals("=")) {
                            if ( compResult==0 ) {
                                listResult.add(obj);
                            }
                        } else if (op.equals(">")) {
                            if ( compResult > 0 ) {
                                listResult.add(obj);
                            }
                        } else if (op.equals(">=")) {
                            if ( compResult >= 0 ) {
                                listResult.add(obj);
                            }
                        } else if (op.equals("<")) {
                            if ( compResult < 0 ) {
                                listResult.add(obj);
                            }
                        } else if (op.equals("<=")) {
                            if ( compResult <= 0 ) {
                                listResult.add(obj);
                            }
                        }
                    }
                }
            } else {
                // TODO: da gestire
                System.out.println("Indirezione con '.' non ancora gestita...");
            }
        }

        return listResult;
    }

    
} 
