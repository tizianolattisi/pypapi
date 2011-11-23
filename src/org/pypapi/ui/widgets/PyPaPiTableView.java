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
package org.pypapi.ui.widgets;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;
import java.util.ArrayList;
import java.util.List;
import org.pypapi.GlobalManager;
import org.pypapi.db.*;
import org.pypapi.ui.*;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.it)
 *
 * PyPaPiTableView is a custom widget implementing info, open, add and delete
 * of rows in the bag list.
 *
 */
public class PyPaPiTableView extends QTableView{

    private QAction actionAdd, actionDel, actionOpen, actionInfo;
    private QMenu menuPopup;

    public PyPaPiTableView(){
        /*
         *  init
         */
        this.initializeContextMenu();
    }
    
    private void initializeContextMenu(){
        this.setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
        this.customContextMenuRequested.connect(this, "contextMenu(QPoint)");
        this.menuPopup = new QMenu(this);

        this.actionInfo = new QAction("Info", this);
        QIcon iconInfo = new QIcon("classpath:org/pypapi/ui/resources/toolbar/information.png");
        this.actionInfo.setIcon(iconInfo);
        this.menuPopup.addAction(actionInfo);

        this.actionOpen = new QAction("Open", this);
        QIcon iconOpen = new QIcon("classpath:org/pypapi/ui/resources/open.png");
        this.actionOpen.setIcon(iconOpen);
        this.menuPopup.addAction(actionOpen);

        this.actionAdd = new QAction("Add", this);
        QIcon iconAdd = new QIcon("classpath:org/pypapi/ui/resources/toolbar/add.png");
        this.actionAdd.setIcon(iconAdd);
        this.menuPopup.addAction(actionAdd);

        this.actionDel = new QAction("Delete", this);
        QIcon iconDel = new QIcon("classpath:org/pypapi/ui/resources/toolbar/delete.png");
        this.actionDel.setIcon(iconDel);
        this.menuPopup.addAction(actionDel);


    }


    private void contextMenu(QPoint point){
        QAction action = this.menuPopup.exec(this.mapToGlobal(point));
        if (this.actionOpen.equals(action)){
            List<QModelIndex> rows = this.selectionModel().selectedRows();
            for (QModelIndex idx: rows){
                TableModel model = (TableModel) this.model();
                Object entity = model.getEntityByRow(idx.row());
                List entities = new ArrayList();
                entities.add(entity);
                Store store = new Store(entities);
                Form form = (Form) GlobalManager.queryUtility(Form.class, entity.getClass().getName());
                Form newForm = new Form(form);
                newForm.init(store);
                newForm.show();
            }
        }
    }

}
