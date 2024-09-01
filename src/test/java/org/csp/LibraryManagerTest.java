package org.csp;

import org.csp.exceptions.DuplicateISBNException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryManagerTest {

    @Mock
    private BookDao bookDao;
    @InjectMocks
    private LibraryManager libraryManager;

    @BeforeEach
    void setUp() {
        libraryManager = new LibraryManager(bookDao);
    }

    @Test
    void addBookShouldInsertASingleBook() throws DuplicateISBNException {
        Book book = new Book("9789353008956", "Logic Design", "Dr. Chirag", 2003);
        when(bookDao.existsByIsbn(book.getIsbn())).thenReturn(false);
        libraryManager.addBook(book);
        Mockito.verify(bookDao, times(1)).insert(book);
    }

    @Test
    void addBookShouldNotInsertBookWithDuplicateISBN() {
        Book book = new Book("9789353008956", "Logic Design", "Dr. Chirag", 2003);
        when(bookDao.existsByIsbn(book.getIsbn())).thenReturn(true);
        DuplicateISBNException duplicateISBNException = assertThrows(DuplicateISBNException.class, () -> libraryManager.addBook(book));
        Assertions.assertEquals(duplicateISBNException, new DuplicateISBNException(book.getIsbn()));
    }
}