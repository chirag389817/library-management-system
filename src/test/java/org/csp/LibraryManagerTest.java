package org.csp;

import org.csp.exceptions.BookAlreadyBorrowedException;
import org.csp.exceptions.BookNotBorrowedException;
import org.csp.exceptions.BookNotFoundException;
import org.csp.exceptions.DuplicateISBNException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryManagerTest {

    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private LibraryManager libraryManager;

    @BeforeEach
    void setUp() {
        libraryManager = new LibraryManager(bookRepository);
    }

    @Test
    void addBookShouldInsertASingleBook() throws DuplicateISBNException {
        Book book = new Book("9789353008956", "Logic Design", "Dr. Chirag", 2003);
        when(bookRepository.existsByIsbn(book.getIsbn())).thenReturn(false);
        libraryManager.addBook(book);
        Mockito.verify(bookRepository, times(1)).insert(book);
    }

    @Test
    void addBookShouldNotInsertBookWithDuplicateISBN() {
        Book book = new Book("9789353008956", "Logic Design", "Dr. Chirag", 2003);
        when(bookRepository.existsByIsbn(book.getIsbn())).thenReturn(true);
        DuplicateISBNException duplicateISBNException = assertThrows(DuplicateISBNException.class, () -> libraryManager.addBook(book));
        Assertions.assertEquals(duplicateISBNException, new DuplicateISBNException(book.getIsbn()));
    }

    @Test
    void borrowBookShouldAllowBookToBorrow() throws BookNotFoundException, BookAlreadyBorrowedException {
        Book book = new Book("9789353008956", "Logic Design", "Dr. Chirag", 2003);
        book.setBorrowed(false);
        when(bookRepository.getByIsbn(book.getIsbn())).thenReturn(book);
        libraryManager.borrowBook(book.getIsbn());
        book.setBorrowed(true);
        Mockito.verify(bookRepository, times(1)).updateBook(book);
    }

    @Test
    void borrowBookShouldNotAllowABookTOBeBorrowedWhichDoesNotExists() {
        when(bookRepository.getByIsbn("1234567890123")).thenReturn(null);
        BookNotFoundException bookNotFoundException = assertThrows(BookNotFoundException.class, () -> libraryManager.borrowBook("1234567890123"));
        Assertions.assertEquals(bookNotFoundException, new BookNotFoundException("1234567890123"));
    }

    @Test
    void borrowBookShouldNotAllowABookTOBeBorrowedWhichIsAlreadyBorrowed() {
        Book book = new Book("9789353008956", "Logic Design", "Dr. Chirag", 2003);
        book.setBorrowed(true);
        when(bookRepository.getByIsbn(book.getIsbn())).thenReturn(book);
        BookAlreadyBorrowedException bookAlreadyBorrowedException = assertThrows(BookAlreadyBorrowedException.class, () -> libraryManager.borrowBook(book.getIsbn()));
        Assertions.assertEquals(bookAlreadyBorrowedException, new BookAlreadyBorrowedException(book.getIsbn()));
    }

    @Test
    void returnBookShouldAllowBookToReturn() throws BookNotFoundException, BookNotBorrowedException {
        Book book = new Book("9789353008956", "Logic Design", "Dr. Chirag", 2003);
        book.setBorrowed(true);
        when(bookRepository.getByIsbn(book.getIsbn())).thenReturn(book);
        libraryManager.returnBook(book.getIsbn());
        book.setBorrowed(false);
        Mockito.verify(bookRepository, times(1)).updateBook(book);
    }

    @Test
    void returnBookShouldNotAllowABookToReturnWhichDoesNotExists() {
        when(bookRepository.getByIsbn("1234567890123")).thenReturn(null);
        BookNotFoundException bookNotFoundException = assertThrows(BookNotFoundException.class, () -> libraryManager.returnBook("1234567890123"));
        Assertions.assertEquals(bookNotFoundException, new BookNotFoundException("1234567890123"));
    }

    @Test
    void returnBookShouldNotAllowABookToReturnWhichIsNotBorrowed() {
        Book book = new Book("9789353008956", "Logic Design", "Dr. Chirag", 2003);
        book.setBorrowed(false);
        when(bookRepository.getByIsbn(book.getIsbn())).thenReturn(book);
        BookNotBorrowedException bookNotBorrowedException = assertThrows(BookNotBorrowedException.class, () -> libraryManager.returnBook(book.getIsbn()));
        Assertions.assertEquals(bookNotBorrowedException, new BookNotBorrowedException(book.getIsbn()));
    }

    @Test
    void getAvailableBooksShouldReturnEmptyList(){
        when(bookRepository.getBooks(false)).thenReturn(new ArrayList<>());
        List<Book> availableBooks = libraryManager.getAvailableBooks();
        Assertions.assertNotNull(availableBooks);
        assertEquals(availableBooks.size(), 0);
    }

    @Test
    void getAvailableBooksShouldReturnOne(){
        Book book = new Book("9789353008956", "Logic Design", "Dr. Chirag", 2003);
        when(bookRepository.getBooks(false)).thenReturn(Arrays.asList(book));
        List<Book> availableBooks = libraryManager.getAvailableBooks();
        assertAll(
                ()->Assertions.assertNotNull(availableBooks),
                ()->assertEquals(availableBooks.size(), 1),
                ()->assertEquals(availableBooks.get(0).getIsbn(),book.getIsbn())
                );
    }

    @Test
    void getAvailableBooksShouldIgnoreBorrowedBooks(){
        Book book1 = new Book("9789353008956", "Logic Design", "Dr. Chirag", 2003);
        Book book2 = new Book("9789353008957", "Logic Design", "Dr. Chirag", 2003);
        book1.setBorrowed(true);
        when(bookRepository.getBooks(false)).thenReturn(Arrays.asList(book2));
        List<Book> availableBooks = libraryManager.getAvailableBooks();
        assertAll(
                ()->verify(bookRepository).getBooks(false),
                ()->Assertions.assertNotNull(availableBooks),
                ()->assertEquals(availableBooks.size(), 1),
                ()->assertEquals(availableBooks.get(0).getIsbn(),book2.getIsbn())
        );
    }
}