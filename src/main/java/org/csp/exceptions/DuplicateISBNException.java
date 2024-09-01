package org.csp.exceptions;

public class DuplicateISBNException extends Exception{
    private String isbn;

    public DuplicateISBNException(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return "ISBN: " + isbn + " is already exists";
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(((DuplicateISBNException)obj).toString());
    }
}
