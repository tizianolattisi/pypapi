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
package com.axiastudio.pypapi.ui;

import com.axiastudio.pypapi.Register;
import com.axiastudio.pypapi.ui.widgets.PyPaPiTableView;
import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.gui.*;
import java.util.List;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Delegate extends QItemDelegate {

    /*
     * The PyPaPi Delegate need a QTablewView as parent
     */
    public Delegate(QTableView parent) {
        super(parent);
    }

    @Override
    public QWidget createEditor(QWidget qw, QStyleOptionViewItem qsovi, QModelIndex qmi) {
        Object data = qmi.data();
        Column column = ((TableModel) qmi.model()).getColumns().get(qmi.column());
        if( column.getEditorType() == CellEditorType.STRING ){
            QLineEdit lineEdit = new QLineEdit(qw);
            return lineEdit;
        } else if( column.getEditorType() == CellEditorType.INTEGER ){
            QSpinBox spinBox = new QSpinBox(qw);
            return spinBox;
        } else if( column.getEditorType() == CellEditorType.BOOLEAN ){
            return super.createEditor(qw, qsovi, qmi);
        } else if( column.getEditorType() == CellEditorType.DATE ){
            
        } else if( column.getEditorType() == CellEditorType.CHOICE ){
            // TODO: QComboBox from Enum
            //QComboBox cb = new QComboBox(qw);
            //return cb;
        } else if( column.getEditorType() == CellEditorType.LOOKUP ){
            
        }
        return super.createEditor(qw, qsovi, qmi);
    }

    @Override
    public void setEditorData(QWidget qw, QModelIndex qmi) {        
        super.setEditorData(qw, qmi);
    }

    @Override
    public void setModelData(QWidget qw, QAbstractItemModel qaim, QModelIndex qmi) {
        Column column = ((TableModel) qmi.model()).getColumns().get(qmi.column());
        if( column.getEditorType() == CellEditorType.STRING ){
            String value = ((QLineEdit) qw).text();
            ((TableModel) qaim).setData(qmi, value);
        } else if( column.getEditorType() == CellEditorType.INTEGER ){
            Integer value = ((QSpinBox) qw).value();
            ((TableModel) qaim).setData(qmi, value);
        } else if( column.getEditorType() == CellEditorType.BOOLEAN ){
            super.setModelData(qw, qaim, qmi);
        } else if( column.getEditorType() == CellEditorType.DATE ){
        } else if( column.getEditorType() == CellEditorType.CHOICE ){
        } else if( column.getEditorType() == CellEditorType.LOOKUP ){
        }
        //super.setModelData(qw, qaim, qmi);
    }

    @Override
    public void updateEditorGeometry(QWidget qw, QStyleOptionViewItem qsovi, QModelIndex qmi) {
        super.updateEditorGeometry(qw, qsovi, qmi);
    }

    
}
