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
import org.pypapi.db.Database;
import org.pypapi.demo.Author;
import org.pypapi.demo.Book;

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

        // inizializzazione Qt
        QApplication.initialize(args);

        // inizializazione database
        Database db = new Database();
        db.open();

        // creo e mostro la form
        //QFile booksUiFile = new QFile("classpath:org/pypapi/demo/books.jui");
        //Form booksForm = new Form(booksUiFile, Book.class);
        //booksForm.show();
        QFile authorsUiFile = new QFile("classpath:org/pypapi/demo/authors.jui");
        Form authorsForm = new Form(authorsUiFile, Author.class);
        authorsForm.show();

        QApplication.exec();

    }
}
