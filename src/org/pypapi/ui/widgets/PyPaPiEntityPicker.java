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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.pypapi.GlobalManager;
import org.pypapi.db.Controller;
import org.pypapi.db.IController;
import org.pypapi.ui.Util;
import org.pypapi.ui.Column;
import org.pypapi.ui.Context;
import org.pypapi.ui.Form;
import org.pypapi.ui.ItemLookup;
import org.pypapi.ui.PickerDialog;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.it)
 */
public class PyPaPiEntityPicker extends QLineEdit{
    
    private Column column;
    private QAction actionOpen, actionSelect;
    private QMenu menuPopup;

    public PyPaPiEntityPicker(){
        /*
         *  init
         */
        this.initializeContextMenu();
    }

    private void initializeContextMenu(){
        this.setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
        this.customContextMenuRequested.connect(this, "contextMenu(QPoint)");
        this.menuPopup = new QMenu(this);

        this.actionSelect = new QAction("Select", this);
        QIcon iconSelect = new QIcon("classpath:org/pypapi/ui/resources/link.png");
        this.actionSelect.setIcon(iconSelect);
        this.menuPopup.addAction(actionSelect);

        this.actionOpen = new QAction("Open", this);
        QIcon iconOpen = new QIcon("classpath:org/pypapi/ui/resources/open.png");
        this.actionOpen.setIcon(iconOpen);
        this.menuPopup.addAction(actionOpen);
        
        this.installEventFilter(this);
        
    }


    private void contextMenu(QPoint point){
        QAction action = this.menuPopup.exec(this.mapToGlobal(point));
        if (this.actionOpen.equals(action)){
            Context context = (Context) GlobalManager.queryRelation(this.window(), ".");
            try {
                ItemLookup item = (ItemLookup) context.getModel().getByEntity(context.getCurrentEntity(), column);
                Object entity = item.get();
                Form newForm = Util.formFromEntity(entity);
                newForm.show();
            } catch (Exception ex) {
                Logger.getLogger(PyPaPiEntityPicker.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (this.actionSelect.equals(action)){
            // XXX: demo Author selection
            Controller controller = (Controller) GlobalManager.queryUtility(IController.class, "org.pypapi.demo.entities.Author");
            PickerDialog pd = new PickerDialog(this, controller);
        int res = pd.exec();
        if ( res == 1 ){
            System.out.println(pd.getSelection());
        }

        }
    }
    
    @Override
    public boolean eventFilter(QObject dist, QEvent event){
        // TODO: fast selector from id?
        return false;
    }

    
    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

}
