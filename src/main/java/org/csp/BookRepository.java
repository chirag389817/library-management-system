package org.csp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class BookRepository {

    private SessionFactory sessionFactory;

    public BookRepository() {
        this.sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public void insert(Book book){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(book);
        transaction.commit();
        session.close();
    }
    public boolean existsByIsbn(String isbn){
        Session session = sessionFactory.openSession();
        Query<Long> query = session.createQuery("SELECT COUNT(b) FROM Book b WHERE b.isbn = :isbn", Long.class);
        query.setParameter("isbn", isbn);
        Long count = query.uniqueResult();
        session.close();
        return count != null && count > 0;
    }

    public Book getByIsbn(String isbn){
        Session session = sessionFactory.openSession();
        Query<Book> query = session.createQuery("FROM Book b WHERE b.isbn = :isbn", Book.class);
        query.setParameter("isbn", isbn);
        Book book = query.uniqueResult();
        session.close();
        return book;
    }

    public void updateBook(Book book){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.merge(book);
        transaction.commit();
        session.close();
    }
}
