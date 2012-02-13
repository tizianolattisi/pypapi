/*
 * Copyright (C) 2011 AXIA Studio (http://www.axiastudio.com)
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
package com.axiastudio.pypapi.ui.widgets;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.Resolver;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.IController;
import com.axiastudio.pypapi.ui.Form;
import com.axiastudio.pypapi.ui.PickerDialog;
import com.axiastudio.pypapi.ui.TableModel;
import com.axiastudio.pypapi.ui.Util;

/**
 *
 * @author tiziano
 *
 * PyPaPiTableView is a custom widget implementing info, open, add and delete
 * of rows in the bag list.
 *
 */
public class PyPaPiTableView extends QTableView{

    private QAction actionAdd, actionDel, actionOpen, actionInfo;
    private QMenu menuPopup;

    public PyPaPiTableView(){
        this(null);
    }

    public PyPaPiTableView(QWidget parent){
        /*
         *  init
         */
        this.setSelectionBehavior(SelectionBehavior.SelectRows);
        this.initializeContextMenu();        
    }
    
    private void initializeContextMenu(){
        this.setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
        this.customContextMenuRequested.connect(this, "contextMenu(QPoint)");
        this.menuPopup = new QMenu(this);

        this.actionInfo = new QAction("Info", this);
        QIcon iconInfo = new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/information.png");
        this.actionInfo.setIcon(iconInfo);
        this.menuPopup.addAction(actionInfo);

        this.actionOpen = new QAction("Open", this);
        QIcon iconOpen = new QIcon("classpath:com/axiastudio/pypapi/ui/resources/open.png");
        this.actionOpen.setIcon(iconOpen);
        this.menuPopup.addAction(actionOpen);

        this.actionAdd = new QAction("Add", this);
        QIcon iconAdd = new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/add.png");
        this.actionAdd.setIcon(iconAdd);
        this.menuPopup.addAction(actionAdd);

        this.actionDel = new QAction("Delete", this);
        QIcon iconDel = new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/delete.png");
        this.actionDel.setIcon(iconDel);
        this.menuPopup.addAction(actionDel);


    }


    private void contextMenu(QPoint point){
        TableModel model = (TableModel) this.model();
        Class rootClass = model.getContextHandle().getRootClass();
        String entityName = (String) this.property("entity");
        Class collectionClass = Resolver.collectionClassFromReference(rootClass, entityName.substring(1));
        List<QModelIndex> rows = this.selectionModel().selectedRows();
        Boolean selected = !rows.isEmpty();
        Object reference = Register.queryRelation(this, "reference");
        this.actionInfo.setEnabled(selected);
        this.actionOpen.setEnabled(selected);
        this.actionDel.setEnabled(selected);
        QAction action = this.menuPopup.exec(this.mapToGlobal(point));
        if (this.actionOpen.equals(action)){
            for (QModelIndex idx: rows){
                Object entity = model.getEntityByRow(idx.row());
                if ( reference != null ){
                    entity = Resolver.entityFromReference(entity, (String) reference);
                }
                Form form = Util.formFromEntity(entity);
                form.show();
            }
        } else if (this.actionAdd.equals(action)){
            if ( reference != null ){
                String name = (String) reference;
                String className = Resolver.entityClassFromReference(collectionClass, (String) reference).getName();
                Controller controller = (Controller) Register.queryUtility(IController.class, className, true);
                PickerDialog pd = new PickerDialog(this, controller);
                int res = pd.exec();
                if ( res == 1 ){
                    if( pd.getSelection().size()>0 ){
                        Object entity = pd.getSelection().get(0);
                        Class<?> ifaceFrom = Resolver.interfaceFromEntityClass(entity.getClass());
                        Class<?> ifaceTo = Resolver.interfaceFromEntityClass(collectionClass);
                        // TODO: I need an adapter...
                        if ( ifaceFrom != null && ifaceTo != null){
                            Method adapter = (Method) Register.queryAdapter(ifaceFrom, ifaceTo);
                            try {
                                Object adapted = adapter.invoke(null, entity);
                                model.getContextHandle().insertElement(adapted);
                            } catch (IllegalAccessException ex) {
                                Logger.getLogger(PyPaPiTableView.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IllegalArgumentException ex) {
                                Logger.getLogger(PyPaPiTableView.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InvocationTargetException ex) {
                                Logger.getLogger(PyPaPiTableView.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            } else {
                // TODO: use the factory to create the row
            }
        }
    }
    
    @Override
    protected void enterEvent(QEvent event){
        //QPoint point = new QPoint(-50, -50);
        //this.contextMenu(point);
    }

    @Override
    protected void leaveEvent(QEvent event){
        //this.menuPopup.close();
    }

}
