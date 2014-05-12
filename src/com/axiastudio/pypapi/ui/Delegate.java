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

import com.trolltech.qt.core.*;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.gui.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Delegate extends QItemDelegate {
    
    private static final Boolean READONLY=true;

    /*
     * The PyPaPi Delegate need a QTablewView as parent
     */
    public Delegate(QTableView parent) {
        super(parent);
    }

    /*
    @Override
    public QWidget createEditor(QWidget qw, QStyleOptionViewItem qsovi, QModelIndex qmi) {
        if( this.READONLY ){
            return null;
        }
        Object data = qmi.data();
        Column column = ((TableModel) qmi.model()).getColumns().get(qmi.column());
        if( column.getEditorType() == CellEditorType.STRING ){
            QLineEdit lineEdit = new QLineEdit(qw);
            return lineEdit;
        } else if( column.getEditorType() == CellEditorType.INTEGER ){
            QSpinBox spinBox = new QSpinBox(qw);
            return spinBox;
        } else if( column.getEditorType() == CellEditorType.DOUBLE || column.getEditorType() == CellEditorType.DECIMAL ){
            QDoubleSpinBox doubleSpinBox = new QDoubleSpinBox(qw);
            return doubleSpinBox;
        } else if( column.getEditorType() == CellEditorType.BOOLEAN ){
            return null;
        } else if( column.getEditorType() == CellEditorType.DATE ){
            
        } else if( column.getEditorType() == CellEditorType.CHOICE ){

        } else if( column.getEditorType() == CellEditorType.LOOKUP ){
            
        }
        return super.createEditor(qw, qsovi, qmi);
    }*/

    /*
    @Override
    public void setEditorData(QWidget qw, QModelIndex qmi) {        
        super.setEditorData(qw, qmi);
    }*/

    /*
    @Override
    public void setModelData(QWidget qw, QAbstractItemModel qaim, QModelIndex qmi) {
        Column column = ((TableModel) qmi.model()).getColumns().get(qmi.column());
        if( column.getEditorType() == CellEditorType.STRING ){
            String value = ((QLineEdit) qw).text();
            ((TableModel) qaim).setData(qmi, value);
        } else if( column.getEditorType() == CellEditorType.INTEGER ){
            Integer value = ((QSpinBox) qw).value();
            ((TableModel) qaim).setData(qmi, value);
        } else if( column.getEditorType() == CellEditorType.DOUBLE || column.getEditorType() == CellEditorType.DECIMAL ){
            Double doubleValue = ((QDoubleSpinBox) qw).value();
            BigDecimal value = BigDecimal.valueOf(doubleValue);
            ((TableModel) qaim).setData(qmi, value);
        } else if( column.getEditorType() == CellEditorType.CHOICE ){
            // disabled
        } else if( column.getEditorType() == CellEditorType.DATE ){
        } else if( column.getEditorType() == CellEditorType.BOOLEAN ){
            super.setModelData(qw, qaim, qmi);
            //Qt.CheckState checkState = ((QCheckBox) qw).checkState();
            //Boolean value = null;
            //if(checkState == Qt.CheckState.Checked){
            //    value = true;
            //} else if (checkState == Qt.CheckState.Unchecked) {
            //    value = false;
            //}          
            //((TableModel) qaim).setData(qmi, value);
        } else if( column.getEditorType() == CellEditorType.LOOKUP ){
            // disabled
        }
        //super.setModelData(qw, qaim, qmi);
    }*/

    /*
    @Override
    public void updateEditorGeometry(QWidget qw, QStyleOptionViewItem qsovi, QModelIndex qmi) {
        super.updateEditorGeometry(qw, qsovi, qmi);
    }*/

    
    @Override
    public void paint(QPainter painter, QStyleOptionViewItem option, QModelIndex index) {
        /*
        TableModel model=null;
        if( index.model() instanceof ProxyModel ){
            model = (TableModel) ((ProxyModel) index.model()).sourceModel();
        } else {
            model = (TableModel) index.model();
        }*/
        ITableModel model = (ITableModel) index.model();
        Column column = model.getColumns().get(index.column());
        if( column.getEditorType() == CellEditorType.DATE ){
            Object data = model.data(index, Qt.ItemDataRole.DisplayRole);
            String out=null;
            if( data instanceof QDate ){
                QDate minimumDate = (new QDateEdit()).minimumDate();
                QDate date = (QDate) data; //model.data(index, Qt.ItemDataRole.DisplayRole);
                if( date.equals(minimumDate) ){
                    out = "";
                } else {
                    out = date.toString("dd/MM/yyyy");
                }
            } else if( data instanceof QDateTime ){
                QDateTime date = (QDateTime) data; //model.data(index, Qt.ItemDataRole.DisplayRole);
                QDateTime minimumDateTime = (new QDateEdit()).minimumDateTime();
                if( date.equals(minimumDateTime) ){
                    out = "";
                } else {
                    out = date.toString("dd/MM/yyyy HH:mm");
                }
            }
            if( out != null ){
                Alignment flags = Qt.AlignmentFlag.createQFlags();
                flags.set(Qt.AlignmentFlag.AlignVCenter);
                flags.set(Qt.AlignmentFlag.AlignCenter);
                painter.drawText(option.rect(), flags.value(), out);
            }
        } else if( column.getEditorType() == CellEditorType.DECIMAL ){
            Object o = model.data(index, Qt.ItemDataRole.DisplayRole);
            String out = "";
            if( o != null ) {
                BigDecimal data = new BigDecimal((Double) o);
                NumberFormat itFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
                itFormat.setMinimumFractionDigits(2);
                itFormat.setMaximumFractionDigits(2);
                out = itFormat.format(data.doubleValue());
            }
            Alignment flags = Qt.AlignmentFlag.createQFlags();
            flags.set(Qt.AlignmentFlag.AlignVCenter);
            flags.set(Qt.AlignmentFlag.AlignCenter);
            painter.drawText(option.rect(), flags.value(), out);
        }
        else {
            super.paint(painter, option, index);
        }
    }

}
