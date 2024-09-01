package org.csp;

import org.csp.exceptions.BookAlreadyBorrowedException;
import org.csp.exceptions.BookNotBorrowedException;
import org.csp.exceptions.BookNotFoundException;
import org.csp.exceptions.DuplicateISBNException;

public class LibraryManager {

    private final BookRepository bookRepository;

    public LibraryManager(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBook(Book book) throws DuplicateISBNException {
        if(bookRepository.existsByIsbn(book.getIsbn())) throw new DuplicateISBNException(book.getIsbn());
        bookRepository.insert(book);
    }

    public void borrowBook(String isbn) throws BookNotFoundException, BookAlreadyBorrowedException {
        Book book = bookRepository.getByIsbn(isbn);
        if(book==null) throw new BookNotFoundException(isbn);
        if(book.isBorrowed()) throw new BookAlreadyBorrowedException(book.getIsbn());
        book.setBorrowed(true);
        bookRepository.updateBook(book);
    }

    public void returnBook(String isbn) throws BookNotFoundException, BookNotBorrowedException {
        Book book = bookRepository.getByIsbn(isbn);
        book.setBorrowed(false);
        bookRepository.updateBook(book);
    }
}
