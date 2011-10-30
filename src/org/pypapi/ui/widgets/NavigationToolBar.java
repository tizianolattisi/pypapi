/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pypapi.ui.widgets;

import java.util.*;

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

import org.pypapi.ui.Form;

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
        QAction action = new QAction(actionName, this.parentForm);
        QIcon icon = new QIcon(iconName);
        action.setText(text);
        action.setToolTip(toolTip);
        action.setIcon(icon);
        action.triggered.connect(agent, actionName+"()");
        this.addAction(action);
    }

}
