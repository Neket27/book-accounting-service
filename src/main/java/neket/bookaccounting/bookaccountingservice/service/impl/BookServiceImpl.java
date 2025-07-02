package neket.bookaccounting.bookaccountingservice.service.impl;

import neket.bookaccounting.bookaccountingservice.controller.dto.book.CreateBookDto;
import neket.bookaccounting.bookaccountingservice.controller.dto.book.UpdateBookDto;
import neket.bookaccounting.bookaccountingservice.exeption.NotFoundException;
import lombok.RequiredArgsConstructor;
import neket.bookaccounting.bookaccountingservice.controller.dto.book.BookDto;
import neket.bookaccounting.bookaccountingservice.entity.Author;
import neket.bookaccounting.bookaccountingservice.entity.Book;
import neket.bookaccounting.bookaccountingservice.mapper.BookMapper;
import neket.bookaccounting.bookaccountingservice.repository.AuthorRepository;
import neket.bookaccounting.bookaccountingservice.repository.BookRepository;
import neket.bookaccounting.bookaccountingservice.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;

    @Override
    @Transactional
    public BookDto create(CreateBookDto dto) {
        Author author = authorRepository.findById(dto.authorId())
                .orElseThrow(() -> new NotFoundException("Author with id: {0} not found.", dto.authorId()));

        Book book = bookMapper.toEntity(dto);
        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookDto> list() {
        return bookMapper.toDtoList(bookRepository.findAll());
    }

    @Override
    public BookDto get(Long id) {
        return bookMapper.toDto(find(id));
    }

    @Override
    @Transactional
    public BookDto update(Long id, UpdateBookDto dto) {
        Book book = find(id);
        bookMapper.update(book, dto);

        Author author = authorRepository.findById(dto.authorId())
                .orElseThrow(() -> new NotFoundException("Author with id: {0} not found.", dto.authorId()));
        book.setAuthor(author);

        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    private Book find(Long id){
        return bookRepository.findById(id).orElseThrow(()->new NotFoundException("Book with id: {0} not found.", id));
    }
}

