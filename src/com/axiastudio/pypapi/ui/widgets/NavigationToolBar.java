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

import java.util.*;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

import com.axiastudio.pypapi.ui.Form;

/**
 *
 * @author tiziano
 */
public class NavigationToolBar extends QToolBar {

    private static HashMap buttons;
    private Form parentForm;

    public NavigationToolBar(String title, Form parent){
        this.parentForm = parent;
        this.configButtons();
    }

    private void configButtons(){
        this.insertButton("firstElement", "First element",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/resultset_first.png",
                "Press to show the first element", this.parentForm.getContext());
        this.insertButton("previousElement", "Previous element",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/resultset_previous.png",
                "Press to show the previous element", this.parentForm.getContext());
        this.insertButton("nextElement", "Next element",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/resultset_next.png",
                "Press to show the next element", this.parentForm.getContext());
        this.insertButton("lastElement", "Last element",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/resultset_last.png",
                "Press to show the last element", this.parentForm.getContext());
        this.insertButton("insertElement", "Insert",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/add.png",
                "Discard currente changes", this.parentForm.getContext());
        this.insertButton("deleteElement", "Delete",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/delete.png",
                "Discard currente changes", this.parentForm.getContext());
        this.insertButton("cancelChanges", "Cancel",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/cancel.png",
                "Discard currente changes", this.parentForm.getContext());
        this.insertButton("commitChanges", "Save",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/disk.png",
                "Save current element", this.parentForm.getContext());
        this.insertButton("search", "Search",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/find.png",
                "Search elements", this.parentForm.getContext());
    }

    private void insertButton(String actionName, String text, String iconName,
            String toolTip, QObject agent){
        QAction action = new QAction(this.parentForm);
        QIcon icon = new QIcon(iconName);
        action.setObjectName(actionName);
        action.setText(text);
        action.setToolTip(toolTip);
        action.setIcon(icon);
        action.triggered.connect(agent, actionName+"()");
        action.triggered.connect(this, "refresh()");
        this.parentForm.getContext().getModel().dataChanged.connect(this, "refresh()");
        this.addAction(action);
    }

    public void refresh(){
        /*
         * Refresh enabled status of the buttons
         */
        Boolean atBof = this.parentForm.getContext().getAtBof();
        Boolean atEof = this.parentForm.getContext().getAtEof();
        Boolean isDirty = this.parentForm.getContext().getIsDirty();

        for(QAction action:this.actions()){
            String objName = action.objectName();
            if ("firstElement".equals(objName)){
                action.setEnabled(!isDirty && !atBof);
            } else if("previousElement".equals(objName)){
                action.setEnabled(!isDirty && !atBof);
            } else if("nextElement".equals(objName)){
                action.setEnabled(!isDirty && !atEof);
            } else if("lastElement".equals(objName)){
                action.setEnabled(!isDirty && !atEof);
            } else if("commitChanges".equals(objName)){
                action.setEnabled(isDirty);
            } else if("cancelChanges".equals(objName)){
                action.setEnabled(isDirty);
            } else if("search".equals(objName)){
                action.setEnabled(!isDirty);
            } else if("insertElement".equals(objName)){
                action.setEnabled(!isDirty);
            } else if("deleteElement".equals(objName)){
                action.setEnabled(!isDirty);
            }
        }
    }

}
