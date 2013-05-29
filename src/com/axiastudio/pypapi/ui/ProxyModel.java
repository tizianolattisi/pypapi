/*
 * Copyright (C) 2013 AXIA Studio (http://www.axiastudio.com)
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
 * You should have received a copy of the GNU Afffero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.pypapi.ui;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.gui.QSortFilterProxyModel;
import java.util.List;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class ProxyModel extends QSortFilterProxyModel implements ITableModel {

    @Override
    public List<Column> getColumns() {
        return ((TableModel) this.sourceModel()).getColumns();
    }
    @Override
    public Context getContextHandle() {
        return ((TableModel) this.sourceModel()).getContextHandle();
    }

    @Override
    public Object getEntityByRow(int row) {
        return ((TableModel) this.sourceModel()).getEntityByRow(row);
    }

    @Override
    public boolean removeRows(int row, int count, QModelIndex parent) {
        return ((TableModel) this.sourceModel()).removeRows(row, count, parent);
    }
    
}
