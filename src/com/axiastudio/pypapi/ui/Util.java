/*
 * Copyright (C) 2011 AXIA Studio (http://www.axiastudio.com)
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
package com.axiastudio.pypapi.ui;

import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QFile;
import com.trolltech.qt.core.QTemporaryFile;
import java.util.ArrayList;
import java.util.List;
import com.axiastudio.pypapi.GlobalManager;
import com.axiastudio.pypapi.db.Store;

/**
 *
 * @author tiziano
 */
public class Util {
    
    public static Form formFromEntity(Object entity){
        List entities = new ArrayList();
        entities.add(entity);
        Store store = new Store(entities);
        Form form = (Form) GlobalManager.queryUtility(IForm.class, entity.getClass().getName());
        Form newForm = new Form(form);
        newForm.init(store);
        return newForm;
    }
    
    public static QFile ui2jui(QFile ui){
        /*
         * Convert a ui QFile in a QFile jui.
         * The jui file is a ui file without the <?xml?> tag, and with the language
         * property set to jambi.
         * Something like:
         * sed 's/<ui version="4.0">/<ui version="4.0" language="jambi">/g' $1 | tail -n +2
         */
        String content;
        QTemporaryFile jui;
        long toRead=ui.size();
        String uiTag = "<ui version=\"4.0\" language=\"jambi\">";
        ui.open(new QFile.OpenMode(QFile.OpenModeFlag.ReadOnly,
                                   QFile.OpenModeFlag.Unbuffered));
        toRead -= ui.readLine().size();
        toRead -= ui.readLine().size();
        content = uiTag + "\n" + ui.read(toRead).toString();
        jui = new QTemporaryFile();
        jui.open(new QFile.OpenMode(QFile.OpenModeFlag.WriteOnly,
                                    QFile.OpenModeFlag.Unbuffered));
        jui.write(new QByteArray(content));
        jui.close();
        return jui;
    }

} 
