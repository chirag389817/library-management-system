package org.csp.exceptions;

public class BookAlreadyBorrowedException extends Exception{
    private String isbn;

    public BookAlreadyBorrowedException(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return STR."Book is already borrowed using ISBN: \{isbn}";
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }
}
