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
package com.axiastudio.pypapi;

import com.trolltech.qt.core.QTranslator;
import com.trolltech.qt.gui.QApplication;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Tiziano Lattisi <tiziano at axiastudio.it>
 */
public class Application extends QApplication {
    
    private String customApplicationName=null;
    private String customApplicationCredits=null;
    private Map<String, Object> config = new HashMap();
    List<String> qmFiles = new ArrayList();
    
    public Application(String[] args){
        super(args);
    }
    
    public void addQmFile(String fileName){
        qmFiles.add(fileName);
    }
    
    public void setLanguage(String lang){        
        QTranslator fwTranslator = new QTranslator(this);
        fwTranslator.load("classpath:com/axiastudio/pypapi/lang/pypapi_"+lang+".qm");
        QApplication.installTranslator(fwTranslator);

        for( String qmFile: qmFiles ){
            if( qmFile.contains("{0}") ){
                qmFile = MessageFormat.format(qmFile, lang);
            }
            QTranslator translator = new QTranslator(this);
            translator.load(qmFile);
            QApplication.installTranslator(translator);
        }
    }

    public String getCustomApplicationCredits() {
        return customApplicationCredits;
    }

    public void setCustomApplicationCredits(String customApplicationCredits) {
        this.customApplicationCredits = customApplicationCredits;
    }

    public String getCustomApplicationName() {
        return customApplicationName;
    }

    public void setCustomApplicationName(String customApplicationName) {
        this.customApplicationName = customApplicationName;
    }
    
    public static Application getApplicationInstance(){
        return (Application) Application.instance();
    }

    public void setConfig(Map<String, Object> config) {
        this.config = config;
    }
    
    public Object getConfigItem(String key) {
        return config.get(key);
    }
    
    public void setConfigItem(String key, Object value) {
        config.put(key, value);
    }
    
}
