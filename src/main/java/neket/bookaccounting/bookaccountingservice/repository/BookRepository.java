package neket.bookaccounting.bookaccountingservice.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import neket.bookaccounting.bookaccountingservice.entity.Book;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Book save(Book book) {
        if (book.getId() == null) {
            em.persist(book);
            return book;
        } else {
            return em.merge(book);
        }
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    public List<Book> findAll() {
        return em.createQuery("SELECT b FROM Book b", Book.class).getResultList();
    }

    public void deleteById(Long id) {
        Book book = em.find(Book.class, id);
        if (book != null) em.remove(book);
    }
}
