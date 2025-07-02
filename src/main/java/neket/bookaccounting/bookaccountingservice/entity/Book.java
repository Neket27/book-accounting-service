package neket.bookaccounting.bookaccountingservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(name = "book_year")
    private Integer year;

    private String genre;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id")
    private Author author;
}
