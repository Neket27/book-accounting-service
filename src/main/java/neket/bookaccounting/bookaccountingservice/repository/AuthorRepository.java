package neket.bookaccounting.bookaccountingservice.repository;

import neket.bookaccounting.bookaccountingservice.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}