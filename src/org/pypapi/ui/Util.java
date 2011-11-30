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

import com.trolltech.qt.core.QByteArray;
import com.trolltech.qt.core.QFile;
import java.util.ArrayList;
import java.util.List;
import org.pypapi.GlobalManager;
import org.pypapi.db.Store;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.it)
 */
public class Util {
    
    public static Form formFromEntity(Object entity){
        List entities = new ArrayList();
        entities.add(entity);
        Store store = new Store(entities);
        Form form = (Form) GlobalManager.queryUtility(Form.class, entity.getClass().getName());
        Form newForm = new Form(form);
        newForm.init(store);
        return newForm;
    }
    
    public static QFile ui2juiQFile(String ui){
        /*
         * Convert a ui in a QFile jui.
         * The jui file is a ui file without the <?xml?> tag, and with the language
         * property set to jambi.
         */
        String output=null;
        String uiTag = "<ui version=\"4.0\" language=\"jambi\">";
        QFile file = new QFile(ui);
        file.open(QFile.OpenModeFlag.ReadOnly);
        file.readLine();
        file.readLine();
        output = file.read(file.size()).toString();
        file.close();
        String juiFileName = ui.substring(0, ui.length()-2)+"jui";
        QFile out = new QFile(juiFileName);
        out.open(QFile.OpenModeFlag.WriteOnly);
        out.write(new QByteArray(uiTag+"\n"));
        out.write(new QByteArray(output));
        out.close();        
        QFile res = new QFile(juiFileName);
        return res;
    }

}
