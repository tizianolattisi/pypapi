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
package org.pypapi;

import com.trolltech.qt.gui.*;
import com.trolltech.qt.core.*;

import org.pypapi.ui.*;
import org.pypapi.db.*;
import org.pypapi.demo.Author;
import org.pypapi.demo.AuthorJpaController;
import org.pypapi.demo.Book;
import org.pypapi.demo.BookJpaController;

/**
 *
 * @author AXIA Studio (http://www.axiastudio.it)
 */
public class PyPaPiStart {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("PyPaPi demo");

        // Qt init
        QApplication.initialize(args);

        // db init
        Database db = new Database();
        db.open("DemoPU");
        
        // create and register Book controller
        BookJpaController jpaController = new BookJpaController(db.entityManagerFactory);
        Controller bookController = new Controller(jpaController, "Book");
        GlobalManager.registerUtility(bookController, IController.class, "org.pypapi.demo.Book");
        // create and register Book form
        QFile booksUiFile = new QFile("classpath:org/pypapi/demo/books.jui");
        Form booksForm = new Form(booksUiFile, Book.class);
        GlobalManager.registerUtility(booksForm, Form.class, "org.pypapi.demo.Book");

        // create and register Author controller
        AuthorJpaController authorJpaController = new AuthorJpaController(db.entityManagerFactory);
        Controller authorController = new Controller(authorJpaController, "Author");
        GlobalManager.registerUtility(authorController, IController.class, "org.pypapi.demo.Author");
        // create and register Author form
        QFile authorsUiFile = new QFile("classpath:org/pypapi/demo/authors.jui");
        Form authorsForm = new Form(authorsUiFile, Author.class);
        GlobalManager.registerUtility(authorsForm, Form.class, "org.pypapi.demo.Author");

        // show Author form
        authorsForm.show();
        
        QApplication.exec();

    }
}
