package neket.bookaccounting.bookaccountingservice.unit;

import neket.bookaccounting.bookaccountingservice.controller.dto.author.AuthorDto;
import neket.bookaccounting.bookaccountingservice.controller.dto.author.CreateAuthorDto;
import neket.bookaccounting.bookaccountingservice.entity.Author;
import neket.bookaccounting.bookaccountingservice.exeption.NotFoundException;
import neket.bookaccounting.bookaccountingservice.mapper.AuthorMapper;
import neket.bookaccounting.bookaccountingservice.repository.AuthorRepository;
import neket.bookaccounting.bookaccountingservice.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository repository;

    @Mock
    private AuthorMapper mapper;

    @InjectMocks
    private AuthorServiceImpl service;

    private Author author;
    private CreateAuthorDto createAuthorDto;
    private AuthorDto authorDto;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1L);
        author.setName("Test Author");
        author.setBirthYear(1900);

        createAuthorDto = CreateAuthorDto.builder()
                .name("Test Author")
                .birthYear(1900)
                .build();

        authorDto = new AuthorDto(1L, "Test Author", 1900);
    }

    @Test
    @DisplayName("Создание автора: должен вернуть AuthorDto")
    void create_shouldReturnAuthorDto() {
        // Arrange
        when(mapper.toEntity(createAuthorDto)).thenReturn(author);
        when(repository.save(author)).thenReturn(author);
        when(mapper.toDto(author)).thenReturn(authorDto);

        // Act
        AuthorDto result = service.create(createAuthorDto);

        // Assert
        assertNotNull(result);
        assertEquals(authorDto.id(), result.id());
        verify(repository).save(author);
        verify(mapper).toEntity(createAuthorDto);
        verify(mapper).toDto(author);
    }

    @Test
    @DisplayName("Получение автора: должен вернуть AuthorDto, если автор существует")
    void get_shouldReturnAuthorDto_whenAuthorExists() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(author));
        when(mapper.toDto(author)).thenReturn(authorDto);

        // Act
        AuthorDto result = service.get(1L);

        // Assert
        assertEquals(authorDto.name(), result.name());
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Получение автора: должен выбросить NotFoundException, если автор не найден")
    void get_shouldThrowNotFoundException_whenAuthorNotExists() {
        // Arrange
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> service.get(999L));
        assertTrue(exception.getMessage().contains("Author with id: 999 not found."));
        verify(repository).findById(999L);
    }

    @Test
    @DisplayName("Получение списка авторов: должен вернуть список AuthorDto с пагинацией")
    void list_shouldReturnPagedAuthors() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 2);
        List<Author> authors = List.of(author, author);
        List<AuthorDto> authorDtos = List.of(authorDto, authorDto);

        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(authors));
        when(mapper.toDtoList(authors)).thenReturn(authorDtos);

        // Act
        List<AuthorDto> result = service.list(pageable);

        // Assert
        assertEquals(2, result.size());
        verify(repository).findAll(pageable);
        verify(mapper).toDtoList(authors);
    }
}
