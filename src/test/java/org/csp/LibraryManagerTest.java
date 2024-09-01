package org.csp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void addBookShouldInsertASingleBook() {
        Book book = new Book("9789353008956", "Logic Design", "Dr. Chirag", 2003);
        libraryManager.addBook(book);
        Mockito.verify(bookDao).insert(book);
    }
}