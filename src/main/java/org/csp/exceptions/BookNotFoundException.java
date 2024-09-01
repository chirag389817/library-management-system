package org.csp.exceptions;

public class BookNotFoundException extends Exception{
    private String isbn;

    public BookNotFoundException(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return STR."Book not found using ISBN: \{isbn}";
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }
}
