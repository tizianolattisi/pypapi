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
package org.pypapi.ui;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.gui.*;
import java.util.ArrayList;
import java.util.List;
import org.pypapi.db.Controller;
import org.pypapi.db.Store;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.it)
 */
public class PickerDialog extends QDialog {

    public List selection;
    private QTableView tableView;
    private QItemSelectionModel selectionModel;
    private QLineEdit filterLineEdit;
    private QLabel searchLogLabel;
    private Store store;
    private Controller controller;

    public PickerDialog(Controller controller) {
        this(null, controller);
    }
    
    public PickerDialog(QWidget parent, Controller controller) {
        this.controller = controller;
        this.selection = new ArrayList();
        this.init();
    }
    
    private void init() {
        this.setWindowTitle("Research and selection");
        QVBoxLayout layout = new QVBoxLayout(this);
        layout.setSpacing(4);
        this.tableView = new QTableView();
        this.tableView.setSizePolicy(new QSizePolicy(QSizePolicy.Policy.Expanding,
                QSizePolicy.Policy.Expanding));
        this.tableView.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        this.tableView.setMinimumHeight(150);
        this.tableView.setSortingEnabled(true);
        layout.addWidget(this.tableView, 1);
        this.filterLineEdit = new QLineEdit();
        QLabel filterLabel = new QLabel();
        filterLabel.setPixmap(new QPixmap("classpath:org/pypapi/ui/resources/toolbar/find.png"));
        this.searchLogLabel = new QLabel();
        QToolButton buttonCancel = new QToolButton(this);
        buttonCancel.setIcon(new QIcon("classpath:org/pypapi/ui/resources/toolbar/cancel.png"));
        buttonCancel.clicked.connect(this, "reject()");
        QToolButton buttonAccept = new QToolButton(this);
        buttonAccept.setIcon(new QIcon("classpath:org/pypapi/ui/resources/toolbar/accept.png"));
        buttonAccept.clicked.connect(this, "accept()");
        QHBoxLayout buttonLayout = new QHBoxLayout();
        buttonLayout.setSpacing(4);
        QSpacerItem spacer = new QSpacerItem(40, 20, QSizePolicy.Policy.Expanding,
                QSizePolicy.Policy.Minimum);
        buttonLayout.addWidget(this.filterLineEdit);
        buttonLayout.addWidget(filterLabel);
        buttonLayout.addItem(spacer);
        buttonLayout.addWidget(buttonCancel);        
        buttonLayout.addWidget(buttonAccept);
        layout.addLayout(buttonLayout);
        this.resize(500, 300);
    }
    
    public void executeSearch(){
        /// XXX: demo with description column and a full superstore
        List columns = new ArrayList();
        Column c = new Column("Description", "Description", "Description");
        columns.add(c);
        Store supersetStore = this.controller.createFullStore();
        TableModel model = new TableModel(supersetStore, columns);
        this.tableView.setModel(model);
        this.selectionModel = new QItemSelectionModel(model);
        this.tableView.setSelectionModel(this.selectionModel);
        this.selectionModel.selectionChanged.connect(this,
                "selectRows(QItemSelection, QItemSelection)");
        
    }
    
    private void selectRows(QItemSelection selected, QItemSelection deselected){
        TableModel model = (TableModel) this.tableView.model();
        for (QModelIndex i: selected.indexes()){
            boolean res = this.selection.add(model.getEntityByRow(i.row()));
        }
        for (QModelIndex i: deselected.indexes()){
            boolean res = this.selection.remove(model.getEntityByRow(i.row()));
        }
    }
    
}
