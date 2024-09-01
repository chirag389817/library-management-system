package org.csp;

public class LibraryManager {

    private final BookDao bookDao;

    public LibraryManager(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public void addBook(Book book){
        bookDao.insert(book);
    }
}
