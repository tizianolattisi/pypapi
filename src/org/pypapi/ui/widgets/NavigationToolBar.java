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

import java.util.*;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

import org.pypapi.ui.Form;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.it)
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
                "classpath:org/pypapi/ui/resources/toolbar/resultset_first.png",
                "Press to show the first element", this.parentForm.context);
        this.insertButton("previousElement", "Previous element",
                "classpath:org/pypapi/ui/resources/toolbar/resultset_previous.png",
                "Press to show the previous element", this.parentForm.context);
        this.insertButton("nextElement", "Next element",
                "classpath:org/pypapi/ui/resources/toolbar/resultset_next.png",
                "Press to show the next element", this.parentForm.context);
        this.insertButton("lastElement", "Last element",
                "classpath:org/pypapi/ui/resources/toolbar/resultset_last.png",
                "Press to show the last element", this.parentForm.context);
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
        this.addAction(action);
    }

    private void refresh(){
        /*
         * Refresh enabled status of the buttons
         */
        Boolean atBof = this.parentForm.context.atBof;
        Boolean atEof = this.parentForm.context.atEof;

        for(QAction action:this.actions()){
            String objName = action.objectName();
            if ("firstElement".equals(objName)){
                action.setEnabled(!atBof);
            } else if("previousElement".equals(objName)){
                action.setEnabled(!atBof);
            } else if("nextElement".equals(objName)){
                action.setEnabled(!atEof);
            } else if("lastElement".equals(objName)){
                action.setEnabled(!atEof);
            }
        }
    }

}
