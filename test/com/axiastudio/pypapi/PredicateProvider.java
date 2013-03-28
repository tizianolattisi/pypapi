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
package com.axiastudio.pypapi;

import com.axiastudio.pypapi.demo.entities.Book;
import com.axiastudio.pypapi.demo.entities.Book_;
import com.axiastudio.pypapi.demo.entities.Genre;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.com)
 */
public class PredicateProvider {
    
    public static Predicate bookPredicateProvider(CriteriaQuery cq, CriteriaBuilder cb) {
        // filter out reference books
        Class<?> returnType = Book.class;
        Root from = cq.from(returnType);
        Predicate predicate = cb.notEqual(from.get(Book_.genre), Genre.REFERENCE);
        return predicate;
    }
    
}
