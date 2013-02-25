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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */


public class JsonUtil {
    
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String DATEFORMAT = "yyyy-MM-dd";

    public static String pojoToJson(Object object){
        return pojoToJson(object, false);
    }
    
    public static String pojoToJson(Object object, Boolean pretty){
        String json=null;
        // Bootstrap date widget format
        DateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
        JsonUtil.mapper.setDateFormat(dateFormat);
        try {
            if( !pretty ){
                json = JsonUtil.mapper.writeValueAsString(object);
            } else {
                json = JsonUtil.mapper.writer().withDefaultPrettyPrinter().writeValueAsString(object);
            }
        } catch (JsonProcessingException ex) {
            Logger.getLogger(JsonUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json;
    }
    
    public static void jsonToPojo(Object object, String json){
        DateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
        JsonUtil.mapper.setDateFormat(dateFormat);
        try {
            JsonUtil.mapper.reader().withValueToUpdate(object).readValue(json);
        } catch (IOException ex) {
            Logger.getLogger(JsonUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void jsonToPojo(Object object, Map map){
        String json = JsonUtil.mapToJson(map);
        JsonUtil.jsonToPojo(object, json);
    }

    
    public static Map<String, Object> jsonToMap(String json){
        Map<String, Object> map=null;
        try {
            map = JsonUtil.mapper.readValue(json, Map.class);
        } catch (IOException ex) {
            Logger.getLogger(JsonUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return map;
    }
    
    public static String mapToJson(Map<String, Object> map){
        String json=null;
        try {
            json = JsonUtil.mapper.writeValueAsString(map);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(JsonUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json;
    }
    
}
