/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pypapi.ui.widgets;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

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
        System.out.println(action);
    }

}
