package org.csp.exceptions;

public class BookNotBorrowedException extends Exception{
    private String isbn;

    public BookNotBorrowedException(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return STR."Book is not borrowed using ISBN: \{isbn}";
    }

    @Override
    public boolean equals(Object obj) {
        return toString().equals(obj.toString());
    }
}
