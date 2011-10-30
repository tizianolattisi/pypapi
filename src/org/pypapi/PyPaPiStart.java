/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author tiziano
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
