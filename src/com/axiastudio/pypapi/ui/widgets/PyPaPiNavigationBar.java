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

import com.axiastudio.pypapi.ui.Window;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QKeySequence;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class PyPaPiNavigationBar extends PyPaPiToolBar {

    public PyPaPiNavigationBar(String title, Window parent){
        super(title, parent);
        this.configButtons();
        this.parentForm.getContext().getModel().dataChanged.connect(this, "refresh()");

    }

    private void configButtons(){
        this.insertButton("firstElement", tr("FIRST_ELEMENT"),
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/resultset_first.png",
                tr("FIRST_ELEMENT_DESCRIPTION"), this.parentForm.getContext(),
                new QKeySequence(QKeySequence.StandardKey.MoveToStartOfDocument)); // XXX ctrl+pg
        this.insertButton("previousElement", tr("PREVIOUS_ELEMENT"),
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/resultset_previous.png",
                tr("PREVIOUS_ELEMENT_DESCRIPTION"), this.parentForm.getContext(),
                new QKeySequence(QKeySequence.StandardKey.MoveToPreviousPage));
        this.insertButton("nextElement", tr("NEXT_ELEMENT"),
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/resultset_next.png",
                tr("NEXT_ELEMENT_DESCRIPTION"), this.parentForm.getContext(),
                new QKeySequence(QKeySequence.StandardKey.MoveToNextPage));
        this.insertButton("lastElement", tr("LAST_ELEMENT"),
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/resultset_last.png",
                tr("LAST_ELEMENT_DESCRIPTION"), this.parentForm.getContext(),
                new QKeySequence(QKeySequence.StandardKey.MoveToEndOfDocument)); // XXX ctrl+pg
        this.insertButton("insertElement", tr("INSERT_ELEMENT"),
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/add.png",
                tr("INSERT_ELEMENT_DESCRIPTION"), this.parentForm.getContext(),
                new QKeySequence(QKeySequence.StandardKey.New));
        this.insertButton("deleteElement", tr("DELETE_ELEMENT"),
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/delete.png",
                tr("DELETE_ELEMENT_DESCRIPTION"), this.parentForm,
                new QKeySequence(QKeySequence.StandardKey.Delete));
        this.insertButton("cancelChanges", tr("CANCEL_CHANGES"),
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/cancel.png",
                tr("CANCEL_CHANGES_DESCRIPTION"), this.parentForm.getContext(),
                new QKeySequence("Escape")); // XXX: does not work?
        this.insertButton("commitChanges", tr("SAVE_CHANGES"),
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/disk.png",
                tr("SAVE_CHANGES_DESCRIPTION"), this.parentForm.getContext(),
                new QKeySequence(QKeySequence.StandardKey.Save));
        this.insertButton("refreshElement", tr("REFRESH"),
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/arrow_refresh.png",
                tr("REFRESH_DESCRIPTION"), this.parentForm.getContext(),
                new QKeySequence(QKeySequence.StandardKey.Refresh));
        this.insertButton("search", tr("SEARCH"),
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/find.png",
                tr("SEARCH_DESCRIPTION"), this.parentForm.getContext(),
                new QKeySequence(QKeySequence.StandardKey.Find));
        this.insertButton("information", tr("INFORMATION"),
                "classpath:com/axiastudio/pypapi/ui/resources/toolbar/information.png",
                tr("INFORMATION_DESCRIPTION"), this.parentForm);
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
        Boolean readOnly = this.parentForm.getContext().getReadOnly();
        Boolean noDelete = this.parentForm.getContext().getNoDelete();
        Boolean noInsert = this.parentForm.getContext().getNoInsert();
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
                action.setEnabled(isDirty && !readOnly);
            } else if ("cancelChanges".equals(objName)) {
                action.setEnabled(isDirty);
            } else if ("search".equals(objName)) {
                //action.setEnabled(!isDirty);
                action.setEnabled(true);
            } else if ("insertElement".equals(objName)) {
                action.setEnabled(!isDirty && !readOnly && !noInsert);
            } else if ("deleteElement".equals(objName)) {
                action.setEnabled(!isDirty && !noDelete && !readOnly);
            } else if ("refreshElement".equals(objName)) {
                action.setEnabled(!isDirty);
            }
        }
    }
}
