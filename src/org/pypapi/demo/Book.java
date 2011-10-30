/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pypapi.demo;

/**
 *
 * @author tiziano
 */
public class Book {
    private String isbn;
    private String title;
    private String description;
    private Author author;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Book(){}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
