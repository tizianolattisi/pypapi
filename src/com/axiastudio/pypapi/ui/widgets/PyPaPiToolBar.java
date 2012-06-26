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

import com.axiastudio.pypapi.ui.Form;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QKeySequence.StandardKey;
import com.trolltech.qt.gui.QToolBar;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class PyPaPiToolBar extends QToolBar {
    protected final Form parentForm;
    protected Map<String, QAction> actions = new HashMap<String, QAction>();

    public PyPaPiToolBar(String title, Form parent) {
        this.parentForm = parent;

    }

    protected QAction insertButton(String actionName, String text, String iconName,
                                String toolTip, QObject agent){
        return this.insertButton(actionName, text, iconName, toolTip, agent, null, null);
    }

    protected QAction insertButton(String actionName, String text, String iconName,
                                String toolTip, QObject agent, StandardKey shortcut){
        return this.insertButton(actionName, text, iconName, toolTip, agent, null, shortcut);
    }

    protected QAction insertButton(String actionName, String text, String iconName,
                                String toolTip, QObject agent, QKeySequence shortcut){
        return this.insertButton(actionName, text, iconName, toolTip, agent, shortcut, null);
    }

    private QAction insertButton(String actionName, String text, String iconName,
                                String toolTip, QObject agent, QKeySequence qks, StandardKey sk){
        QAction action = new QAction(this.parentForm);
        QIcon icon = new QIcon(iconName);
        action.setObjectName(actionName);
        action.setText(text);
        action.setToolTip(toolTip);
        action.setIcon(icon);
        action.triggered.connect(agent, actionName+"()");
        action.triggered.connect(this, "refresh()");
        if( qks != null ){
            action.setShortcut(qks);
        } else if( sk != null ){
            action.setShortcut(sk);
        }
        this.addAction(action);
        this.actions.put(actionName, action);
        return action;
    }
    
    private void configButtons(){}
    
    public void refresh() {}
    
    public QAction actionByName(String name){
        return this.actions.get(name);
    }

    
}
