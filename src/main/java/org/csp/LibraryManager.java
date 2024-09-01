package org.csp;

import org.csp.exceptions.DuplicateISBNException;

public class LibraryManager {

    private final BookRepository bookRepository;

    public LibraryManager(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBook(Book book) throws DuplicateISBNException{
        if(bookRepository.existsByIsbn(book.getIsbn())) throw new DuplicateISBNException(book.getIsbn());
        bookRepository.insert(book);
    }
}
