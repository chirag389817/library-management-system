package org.csp;

import org.csp.exceptions.DuplicateISBNException;

public class LibraryManager {

    private final BookDao bookDao;

    public LibraryManager(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void addBook(Book book) throws DuplicateISBNException{
        if(bookDao.existsByIsbn(book.getIsbn())) throw new DuplicateISBNException(book.getIsbn());
        bookDao.insert(book);
    }
}
