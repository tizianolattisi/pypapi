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
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QKeySequence;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class PyPaPiNavigationBar extends PyPaPiToolBar {

    public PyPaPiNavigationBar(String title, Form parent){
        super(title, parent);
        this.configButtons();
        this.parentForm.getContext().getModel().dataChanged.connect(this, "refresh()");

    }

    private void configButtons(){
        this.insertButton("firstElement", "First element",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/resultset_first.png",
                "Press to show the first element", this.parentForm.getContext(),
                new QKeySequence(QKeySequence.StandardKey.MoveToStartOfDocument)); // XXX ctrl+pg
        this.insertButton("previousElement", "Previous element",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/resultset_previous.png",
                "Press to show the previous element", this.parentForm.getContext(),
                new QKeySequence(QKeySequence.StandardKey.MoveToPreviousPage));
        this.insertButton("nextElement", "Next element",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/resultset_next.png",
                "Press to show the next element", this.parentForm.getContext(),
                new QKeySequence(QKeySequence.StandardKey.MoveToNextPage));
        this.insertButton("lastElement", "Last element",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/resultset_last.png",
                "Press to show the last element", this.parentForm.getContext(),
                new QKeySequence(QKeySequence.StandardKey.MoveToEndOfDocument)); // XXX ctrl+pg
        this.insertButton("insertElement", "Insert",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/add.png",
                "Discard currente changes", this.parentForm.getContext(),
                new QKeySequence(QKeySequence.StandardKey.New));
        this.insertButton("deleteElement", "Delete",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/delete.png",
                "Discard currente changes", this.parentForm.getContext(),
                new QKeySequence(QKeySequence.StandardKey.Delete));
        this.insertButton("cancelChanges", "Cancel",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/cancel.png",
                "Discard currente changes", this.parentForm.getContext(),
                new QKeySequence("Escape")); // XXX: does not work?
        this.insertButton("commitChanges", "Save",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/disk.png",
                "Save current element", this.parentForm.getContext(),
                new QKeySequence(QKeySequence.StandardKey.Save));
        this.insertButton("search", "Search",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/find.png",
                "Search elements", this.parentForm.getContext(),
                new QKeySequence(QKeySequence.StandardKey.Find));
        this.insertButton("information", "Information",
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/information.png",
                "Information on this application", this.parentForm);
        this.insertSeparator(this.actionByName("information"));
    }

    @Override
    public void refresh() {
        /*
         * Refresh enabled status of the buttons
         */
        Boolean atBof = this.parentForm.getContext().getAtBof();
        Boolean atEof = this.parentForm.getContext().getAtEof();
        Boolean isDirty = this.parentForm.getContext().getIsDirty();
        for (QAction action : this.actions()) {
            String objName = action.objectName();
            if ("firstElement".equals(objName)) {
                action.setEnabled(!isDirty && !atBof);
            } else if ("previousElement".equals(objName)) {
                action.setEnabled(!isDirty && !atBof);
            } else if ("nextElement".equals(objName)) {
                action.setEnabled(!isDirty && !atEof);
            } else if ("lastElement".equals(objName)) {
                action.setEnabled(!isDirty && !atEof);
            } else if ("commitChanges".equals(objName)) {
                action.setEnabled(isDirty);
            } else if ("cancelChanges".equals(objName)) {
                action.setEnabled(isDirty);
            } else if ("search".equals(objName)) {
                action.setEnabled(!isDirty);
            } else if ("insertElement".equals(objName)) {
                action.setEnabled(!isDirty);
            } else if ("deleteElement".equals(objName)) {
                action.setEnabled(!isDirty);
            }
        }
    }
}
