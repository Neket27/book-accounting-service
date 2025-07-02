package neket.bookaccounting.bookaccountingservice.service.impl;

import lombok.RequiredArgsConstructor;
import neket.bookaccounting.bookaccountingservice.controller.dto.book.CreateBookDto;
import neket.bookaccounting.bookaccountingservice.controller.dto.book.UpdateBookDto;
import neket.bookaccounting.bookaccountingservice.exeption.NotFoundException;
import neket.bookaccounting.bookaccountingservice.controller.dto.book.BookDto;
import neket.bookaccounting.bookaccountingservice.entity.Author;
import neket.bookaccounting.bookaccountingservice.entity.Book;
import neket.bookaccounting.bookaccountingservice.mapper.BookMapper;
import neket.bookaccounting.bookaccountingservice.repository.AuthorRepository;
import neket.bookaccounting.bookaccountingservice.repository.BookRepository;
import neket.bookaccounting.bookaccountingservice.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    @Override
    @Transactional
    public BookDto create(CreateBookDto dto) {
        logger.info("Creating book with title: {}, authorId: {}", dto.title(), dto.authorId());

        Author author = authorRepository.findById(dto.authorId())
                .orElseThrow(() -> {
                    logger.warn("Author with id {} not found", dto.authorId());
                    return new NotFoundException("Author with id: {0} not found.", dto.authorId());
                });

        Book book = bookMapper.toEntity(dto);
        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);
        logger.info("Book created with id: {}", savedBook.getId());

        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookDto> list() {
        logger.debug("Fetching list of all books");
        return bookMapper.toDtoList(bookRepository.findAll());
    }

    @Override
    public BookDto get(Long id) {
        logger.debug("Fetching book with id: {}", id);
        return bookMapper.toDto(find(id));
    }

    @Override
    @Transactional
    public BookDto update(Long id, UpdateBookDto dto) {
        logger.info("Updating book with id: {}", id);
        Book book = find(id);

        bookMapper.update(book, dto);

        Author author = authorRepository.findById(dto.authorId())
                .orElseThrow(() -> {
                    logger.warn("Author with id {} not found during book update", dto.authorId());
                    return new NotFoundException("Author with id: {0} not found.", dto.authorId());
                });
        book.setAuthor(author);

        Book updatedBook = bookRepository.save(book);
        logger.info("Book with id {} successfully updated", updatedBook.getId());

        return bookMapper.toDto(updatedBook);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        logger.info("Deleting book with id: {}", id);
        bookRepository.deleteById(id);
        logger.info("Book with id {} deleted", id);
    }

    private Book find(Long id) {
        logger.debug("Searching for book with id: {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Book with id {} not found", id);
                    return new NotFoundException("Book with id: {0} not found.", id);
                });
    }
}
