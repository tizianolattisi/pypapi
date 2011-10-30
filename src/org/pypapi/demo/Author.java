/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pypapi.demo;

import java.util.*;

/**
 *
 * @author tiziano
 */
public class Author {
    private Long idAuthor;
    private String name;
    private String surname;
    private List books;

    public List getBooks() {
        return books;
    }

    public void setBooks(List books) {
        this.books = books;
    }

    public Author() {
    }

    public Long getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(Long idAuthor) {
        this.idAuthor = idAuthor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }




}
