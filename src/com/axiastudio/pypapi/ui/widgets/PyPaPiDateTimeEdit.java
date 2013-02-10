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

import com.trolltech.qt.core.*;
import com.trolltech.qt.gui.*;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class PyPaPiDateTimeEdit extends QDateTimeEdit {
    private QMenu menuPopup;
    private QAction actionNull;
    private Boolean isNull;
    private Boolean isInitiallyEnabled=null;
    
    static final String NULLSTYLE="color: white";

    public PyPaPiDateTimeEdit(QWidget parent) {
        super(parent);
        this.init();
    }
    
    public PyPaPiDateTimeEdit() {
        this(null);
    }

    private void init(){
        this.setContextMenuPolicy(Qt.ContextMenuPolicy.CustomContextMenu);
        this.customContextMenuRequested.connect(this, "contextMenu(QPoint)");
        this.menuPopup = new QMenu(this);
        this.actionNull = new QAction(tr("SET_TO_NULL"), this);
        QIcon iconNull = new QIcon("classpath:com/axiastudio/pypapi/ui/resources/toolbar/cancel.png");
        this.actionNull.setIcon(iconNull);
        this.menuPopup.addAction(actionNull);
        this.dateChanged.connect(this, "updateStyleSheet()");
        this.installEventFilter(this);
    }
    
    
    private void contextMenu(QPoint point){
        this.setStyleSheet("");
        QAction action = this.menuPopup.exec(this.mapToGlobal(point));
        if(action.equals(this.actionNull)){
            this.setDateTime(this.minimumDateTime());
            this.setStyleSheet(NULLSTYLE);
        }
    }
    
    
    private void updateStyleSheet(){
        if( this.isInitiallyEnabled == null){
            // XXX: here, because at creation time it's ever enabled.
            this.isInitiallyEnabled = this.isEnabled();
        }
        String style;
        if( this.dateTime().equals(this.minimumDateTime())){
            style = NULLSTYLE;
            this.setEnabled(false);
        } else {
            style = "";
            if( this.isInitiallyEnabled ){
                this.setEnabled(true);
            }
        }
        this.setStyleSheet(style);
    }

    @Override
    public boolean eventFilter(QObject qo, QEvent qevent) {
        if( this.isInitiallyEnabled == null || this.isInitiallyEnabled == false ){
            return super.eventFilter(qo, qevent);
        }
        if( this.dateTime().equals(this.minimumDateTime()) ){
            if( qevent.type() == QEvent.Type.MouseButtonPress ){
                this.setDateTime(QDateTime.currentDateTime());
                this.setFocus();
                this.updateStyleSheet();
                return true;
            }
        } else {
            if( qevent.type() == QEvent.Type.KeyPress ){
                QKeyEvent qkevent = (QKeyEvent) qevent;
                if ( qkevent.key() == Qt.Key.Key_Delete.value() ){
                    this.setDateTime(this.minimumDateTime());
                    this.updateStyleSheet();
                    return true;
                }
            }
            
        }
        return super.eventFilter(qo, qevent);
    }

    
}
