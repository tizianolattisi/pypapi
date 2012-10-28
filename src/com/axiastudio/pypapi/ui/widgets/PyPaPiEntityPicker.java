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

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.db.Controller;
import com.axiastudio.pypapi.db.IController;
import com.axiastudio.pypapi.ui.*;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class PyPaPiEntityPicker extends QLineEdit{
    
    private final String STYLE="QLineEdit {"
            + "image: url(classpath:com/axiastudio/pypapi/ui/resources/cog.png);"
            + "image-position: right; border: 1px solid #999999; }";
    private Column bindColumn;
    private QAction actionOpen, actionSelect;
    private QMenu menuPopup;
    private QToolBar toolBar;

    public PyPaPiEntityPicker(){
        this(null);
    }
    
    public PyPaPiEntityPicker(QWidget parent){
        /*
         *  init
         */
        super(parent);
        this.setStyleSheet(this.STYLE);
        this.initializeContextMenu();        
    }

    private void initializeContextMenu(){
        this.setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
        this.customContextMenuRequested.connect(this, "contextMenu(QPoint)");
        this.menuPopup = new QMenu(this);
        this.toolBar = new QToolBar(this);
        this.toolBar.setOrientation(Qt.Orientation.Horizontal);
        this.toolBar.setIconSize(new QSize(10, 10));
        this.toolBar.hide();

        this.actionSelect = new QAction(tr("SELECT"), this);
        QIcon iconSelect = new QIcon("classpath:com/axiastudio/pypapi/ui/resources/link.png");
        this.actionSelect.setIcon(iconSelect);
        this.menuPopup.addAction(actionSelect);
        this.toolBar.addAction(actionSelect);
        this.actionSelect.triggered.connect(this, "actionSelect()");

        this.actionOpen = new QAction(tr("OPEN"), this);
        QIcon iconOpen = new QIcon("classpath:com/axiastudio/pypapi/ui/resources/open.png");
        this.actionOpen.setIcon(iconOpen);
        this.menuPopup.addAction(actionOpen);
        this.toolBar.addAction(actionOpen);
        this.actionOpen.triggered.connect(this, "actionOpen()");
        
        //this.installEventFilter(this);
        
    }


    private void contextMenu(QPoint point){
        QAction action = this.menuPopup.exec(this.mapToGlobal(point));
    }

    /*
    @Override
    public boolean eventFilter(QObject dist, QEvent event){
        // TODO: fast selector from id?
        return false;
    }
    */

    
    public Column getBindColumn() {
        return bindColumn;
    }

    public void setBindColumn(Column column) {
        this.bindColumn = column;
    }

    @Override
    protected void enterEvent(QEvent event){
        this.toolBar.show();
    }

    @Override
    protected void leaveEvent(QEvent event){
        this.toolBar.hide();
    }

    private void actionOpen(){
        QWidget window = Util.findParentForm(this);
        Context context = (Context) Register.queryRelation(window, ".");
        LookupItemField item;
        try {
            item = (LookupItemField) context.getModel().getByEntity(context.getCurrentEntity(), bindColumn);
        } catch (Exception ex) {
            Logger.getLogger(PyPaPiEntityPicker.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        Object entity = item.get();
        IForm newForm = Util.formFromEntity(entity);
        newForm.show();
    }
    
    private void actionSelect(){
        LookupItemField item;
        QWidget window = Util.findParentForm(this);
        Context context = (Context) Register.queryRelation(window, ".");
        try {
            item = (LookupItemField) context.getModel().getByEntity(context.getCurrentEntity(), bindColumn);
        } catch (Exception ex) {
            Logger.getLogger(PyPaPiEntityPicker.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        Controller controller = (Controller) Register.queryUtility(IController.class, item.getName());
        PickerDialog pd = new PickerDialog(this, controller);
        int res = pd.exec();
        if ( res == 1 ){
            Object value = pd.getSelection().get(0);
            item.set(value);
            // XXX: how to force the repaint?
            context.getDirty();
        }
    }
    
    
}
