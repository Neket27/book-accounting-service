package neket.bookaccounting.bookaccountingservice.unit;

import neket.bookaccounting.bookaccountingservice.controller.dto.book.BookDto;
import neket.bookaccounting.bookaccountingservice.controller.dto.book.CreateBookDto;
import neket.bookaccounting.bookaccountingservice.controller.dto.book.UpdateBookDto;
import neket.bookaccounting.bookaccountingservice.entity.Author;
import neket.bookaccounting.bookaccountingservice.entity.Book;
import neket.bookaccounting.bookaccountingservice.exeption.NotFoundException;
import neket.bookaccounting.bookaccountingservice.mapper.BookMapper;
import neket.bookaccounting.bookaccountingservice.repository.AuthorRepository;
import neket.bookaccounting.bookaccountingservice.repository.BookRepository;
import neket.bookaccounting.bookaccountingservice.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Author author;
    private Book book;
    private BookDto bookDto;
    private CreateBookDto createBookDto;
    private UpdateBookDto updateBookDto;

    @BeforeEach
    void setup() {

        author = new Author();
        author.setId(1L);
        author.setName("Test Author");

        book = new Book();
        book.setId(10L);
        book.setTitle("Test Book");
        book.setAuthor(author);
        book.setYear(2020);
        book.setGenre("Fiction");

        bookDto = BookDto.builder()
                .id(10L)
                .title("Test Book")
                .authorId(1L)
                .year(2020)
                .genre("Fiction")
                .build();

        createBookDto = CreateBookDto.builder()
                .title("Test Book")
                .authorId(1L)
                .year(2020)
                .genre("Fiction")
                .build();

        updateBookDto = UpdateBookDto.builder()
                .title("Updated Book")
                .authorId(1L)
                .year(2021)
                .genre("Sci-Fi")
                .build();
    }

    @Test
    @DisplayName("Создание книги: должен вернуть BookDto, если автор существует")
    void create_shouldReturnBookDto_whenAuthorExists() {
        // Arrange
        when(authorRepository.findById(createBookDto.authorId())).thenReturn(Optional.of(author));
        when(bookMapper.toEntity(createBookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // Act
        BookDto result = bookService.create(createBookDto);

        // Assert
        assertNotNull(result);
        assertEquals(bookDto.id(), result.id());
        assertEquals(bookDto.title(), result.title());
        verify(authorRepository).findById(createBookDto.authorId());
        verify(bookRepository).save(book);
    }

    @Test
    @DisplayName("Создание книги: должен выбросить NotFoundException, если автор не найден")
    void create_shouldThrowNotFoundException_whenAuthorNotFound() {
        // Arrange
        when(authorRepository.findById(createBookDto.authorId())).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> bookService.create(createBookDto));
        assertTrue(ex.getMessage().contains("Author with id: 1 not found."));
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("Получение списка книг: должен вернуть список BookDto")
    void list_shouldReturnListOfBookDto() {
        // Arrange
        List<Book> books = List.of(book);
        List<BookDto> dtos = List.of(bookDto);
        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.toDtoList(books)).thenReturn(dtos);

        // Act
        List<BookDto> result = bookService.list();

        // Assert
        assertEquals(1, result.size());
        assertEquals(bookDto.id(), result.get(0).id());
        verify(bookRepository).findAll();
        verify(bookMapper).toDtoList(books);
    }

    @Test
    @DisplayName("Получение книги: должен вернуть BookDto, если книга найдена")
    void get_shouldReturnBookDto_whenBookExists() {
        // Arrange
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        // Act
        BookDto result = bookService.get(book.getId());

        // Assert
        assertNotNull(result);
        assertEquals(bookDto.id(), result.id());
        verify(bookRepository).findById(book.getId());
    }

    @Test
    @DisplayName("Получение книги: должен выбросить NotFoundException, если книга не найдена")
    void get_shouldThrowNotFoundException_whenBookNotFound() {
        // Arrange
        when(bookRepository.findById(10L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> bookService.get(10L));
        assertTrue(ex.getMessage().contains("Book with id: 10 not found."));
    }

    @Test
    @DisplayName("Обновление книги: должен вернуть обновлённый BookDto, если книга и автор существуют")
    void update_shouldReturnUpdatedBookDto_whenBookAndAuthorExist() {
        // Arrange
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(authorRepository.findById(updateBookDto.authorId())).thenReturn(Optional.of(author));
        doAnswer(invocation -> {
            Book b = invocation.getArgument(0);
            UpdateBookDto dto = invocation.getArgument(1);
            b.setTitle(dto.title());
            b.setYear(dto.year());
            b.setGenre(dto.genre());
            return b;
        }).when(bookMapper).update(any(Book.class), eq(updateBookDto));

        when(bookRepository.save(book)).thenReturn(book);
        BookDto updatedDto = BookDto.builder()
                .id(book.getId())
                .title(updateBookDto.title())
                .authorId(author.getId())
                .year(updateBookDto.year())
                .genre(updateBookDto.genre())
                .build();
        when(bookMapper.toDto(book)).thenReturn(updatedDto);

        // Act
        BookDto result = bookService.update(book.getId(), updateBookDto);

        // Assert
        assertEquals(updateBookDto.title(), result.title());
        assertEquals(updateBookDto.year(), result.year());
        assertEquals(updateBookDto.genre(), result.genre());
        verify(bookRepository).save(book);
    }

    @Test
    @DisplayName("Обновление книги: должен выбросить NotFoundException, если книга не найдена")
    void update_shouldThrowNotFoundException_whenBookNotFound() {
        // Arrange
        when(bookRepository.findById(book.getId())).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> bookService.update(book.getId(), updateBookDto));
        assertTrue(ex.getMessage().contains("Book with id: 10 not found."));
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("Обновление книги: должен выбросить NotFoundException, если автор не найден")
    void update_shouldThrowNotFoundException_whenAuthorNotFound() {
        // Arrange
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(authorRepository.findById(updateBookDto.authorId())).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> bookService.update(book.getId(), updateBookDto));
        assertTrue(ex.getMessage().contains("Author with id: 1 not found."));
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("Удаление книги: должен вызвать deleteById у репозитория")
    void delete_shouldCallRepositoryDeleteById() {
        // Arrange
        doNothing().when(bookRepository).deleteById(book.getId());

        // Act
        bookService.delete(book.getId());

        // Assert
        verify(bookRepository).deleteById(book.getId());
    }

}
