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
import java.util.HashMap;
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
    private QVBoxLayout layout;
    private QToolButton buttonSearch;
    private QToolButton buttonCancel;
    private QToolButton buttonAccept;
    private Boolean isCriteria;
    private HashMap criteriaWidgets;

    public PickerDialog(Controller controller) {
        this(null, controller);
    }
    
    public PickerDialog(QWidget parent, Controller controller) {
        super(parent);
        this.controller = controller;
        this.selection = new ArrayList();
        this.criteriaWidgets = new HashMap();
        this.init();
        List<Column> criteria = ((Form) this.parent()).criteria;
        if (criteria.size()>0){
            this.addCriteria(criteria);
            this.buttonAccept.setEnabled(false);
            this.isCriteria = true;
        } else {
            this.executeSearch();
            this.buttonSearch.setEnabled(false);
            this.isCriteria = false;
        }
    }
    
    private void init(){
        this.setWindowTitle("Research and selection");
        this.layout = new QVBoxLayout(this);
        this.layout.setSpacing(4);
        this.tableView = new QTableView();
        this.tableView.setSizePolicy(new QSizePolicy(QSizePolicy.Policy.Expanding,
                QSizePolicy.Policy.Expanding));
        this.tableView.setSelectionBehavior(QAbstractItemView.SelectionBehavior.SelectRows);
        this.tableView.setMinimumHeight(150);
        this.tableView.setSortingEnabled(true);
        this.layout.addWidget(this.tableView, 1);
        this.filterLineEdit = new QLineEdit();
        QLabel filterLabel = new QLabel();
        filterLabel.setPixmap(new QPixmap("classpath:org/pypapi/ui/resources/toolbar/find.png"));
        this.searchLogLabel = new QLabel();
        this.buttonSearch = new QToolButton(this);
        this.buttonSearch.setIcon(new QIcon("classpath:org/pypapi/ui/resources/key.png"));
        this.buttonSearch.clicked.connect(this, "executeSearch()");
        this.buttonCancel = new QToolButton(this);
        this.buttonCancel.setIcon(new QIcon("classpath:org/pypapi/ui/resources/toolbar/cancel.png"));
        this.buttonCancel.clicked.connect(this, "reject()");
        this.buttonAccept = new QToolButton(this);
        this.buttonAccept.setIcon(new QIcon("classpath:org/pypapi/ui/resources/toolbar/accept.png"));
        this.buttonAccept.clicked.connect(this, "accept()");
        QHBoxLayout buttonLayout = new QHBoxLayout();
        buttonLayout.setSpacing(4);
        buttonLayout.addWidget(this.filterLineEdit);
        buttonLayout.addWidget(filterLabel);
        QSpacerItem spacer = new QSpacerItem(40, 20, QSizePolicy.Policy.Expanding,
                QSizePolicy.Policy.Minimum);
        buttonLayout.addItem(spacer);
        buttonLayout.addWidget(this.buttonSearch);
        QSpacerItem spacer2 = new QSpacerItem(40, 20, QSizePolicy.Policy.Minimum,
                QSizePolicy.Policy.Minimum);
        buttonLayout.addItem(spacer2);
        buttonLayout.addWidget(this.buttonCancel);
        buttonLayout.addWidget(this.buttonAccept);
        this.layout.addLayout(buttonLayout);
        this.resize(500, 300);
    }
    
    private void addCriteria(List<Column> criteria){
        QGridLayout grid = new QGridLayout();
        for (int i=0; i<criteria.size(); i++){
            Column c = criteria.get(i);
            QLabel criteriaLabel = new QLabel(c.label);
            grid.addWidget(criteriaLabel, i, 0);
            QHBoxLayout criteriaLayout = new QHBoxLayout();
            // TODO: different types of search widget depending on the data type
            QLineEdit line = new QLineEdit();
            this.criteriaWidgets.put(c, line);
            criteriaLayout.addWidget(line);
            
            grid.addLayout(criteriaLayout, i, 1);
        }
        this.layout.addLayout(grid);
    }        
    
    public final void executeSearch(){
        List<Column> columns = new ArrayList();
        Store supersetStore=null;
        /// XXX: demo with description column
        Column c = new Column("Description", "Description", "Description");
        columns.add(c);
        if (!this.isCriteria){
            supersetStore = this.controller.createFullStore();
        } else {
            HashMap criteria = new HashMap();
            Form parentForm = (Form) this.parent();
            for (Column criteriaColumn: parentForm.criteria){
                QWidget widget = (QWidget) this.criteriaWidgets.get(criteriaColumn);
                // TODO: criteria with widgets other than QLIneEdit
                if (widget.getClass() == QLineEdit.class){
                    String value = ((QLineEdit) widget).text();
                    if (!"".equals(value)){
                        criteria.put(criteriaColumn, value);
                    }
                }
            }
            supersetStore = this.controller.createCriteriaStore(criteria);
        }
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
        this.buttonAccept.setEnabled(this.selection.size()>0);

    }
    
}
