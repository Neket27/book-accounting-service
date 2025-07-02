package neket.bookaccounting.bookaccountingservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import neket.bookaccounting.bookaccountingservice.controller.dto.author.CreateAuthorDto;
import neket.bookaccounting.bookaccountingservice.controller.dto.book.CreateBookDto;
import neket.bookaccounting.bookaccountingservice.controller.dto.book.UpdateBookDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ExtendWith(SpringExtension.class)
class BookControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long authorId;

    @BeforeEach
    void setupAuthor() throws Exception {
        CreateAuthorDto authorDto = CreateAuthorDto.builder()
                .name("Test Author")
                .birthYear(1970)
                .build();

        String authorJson = mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        authorId = objectMapper.readTree(authorJson).get("id").asLong();
    }

    @Test
    @DisplayName("Создание книги — успешный сценарий")
    void createBookSuccess() throws Exception {
        // Arrange
        CreateBookDto dto = CreateBookDto.builder()
                .title("Test Book")
                .authorId(authorId)
                .year(2023)
                .genre("Fiction")
                .build();

        // Act & Assert
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Test Book")))
                .andExpect(jsonPath("$.authorId", is(authorId.intValue())))
                .andExpect(jsonPath("$.year", is(2023)))
                .andExpect(jsonPath("$.genre", is("Fiction")));
    }

    @Test
    @DisplayName("Создание книги — автор не найден")
    void createBookAuthorNotFound() throws Exception {
        // Arrange
        CreateBookDto dto = CreateBookDto.builder()
                .title("Book Without Author")
                .authorId(999L)
                .year(2025)
                .genre("Fiction")
                .build();

        // Act & Assert
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("Author with id: 999 not found.")));
    }

    @Test
    @DisplayName("Получение книги по ID — успешный сценарий")
    void getBookByIdSuccess() throws Exception {
        // Arrange
        CreateBookDto createDto = CreateBookDto.builder()
                .title("Existing Book")
                .authorId(authorId)
                .year(2020)
                .genre("Drama")
                .build();

        String bookJson = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long bookId = objectMapper.readTree(bookJson).get("id").asLong();

        // Act & Assert
        mockMvc.perform(get("/books/" + bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Existing Book")))
                .andExpect(jsonPath("$.authorId", is(authorId.intValue())))
                .andExpect(jsonPath("$.year", is(2020)))
                .andExpect(jsonPath("$.genre", is("Drama")));
    }

    @Test
    @DisplayName("Получение книги по ID — книга не найдена")
    void getBookByIdNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/books/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Book with id: 999 not found.")));
    }

    @Test
    @DisplayName("Получение списка книг — успешный сценарий")
    void listBooks() throws Exception {
        // Arrange
        for (int i = 0; i < 15; i++) {
            CreateBookDto dto = CreateBookDto.builder()
                    .title("Book " + i)
                    .authorId(authorId)
                    .year(2000 + i)
                    .genre("Genre " + i)
                    .build();

            mockMvc.perform(post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)));
        }

        // Act & Assert
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(15))));
    }

    @Test
    @DisplayName("Обновление книги — успешный сценарий")
    void updateBookSuccess() throws Exception {
        // Arrange
        CreateBookDto createDto = CreateBookDto.builder()
                .title("Old Title")
                .authorId(authorId)
                .year(2001)
                .genre("Old Genre")
                .build();

        String bookJson = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long bookId = objectMapper.readTree(bookJson).get("id").asLong();

        UpdateBookDto updateDto = UpdateBookDto.builder()
                .title("New Title")
                .authorId(authorId)
                .year(2025)
                .genre("New Genre")
                .build();

        // Act & Assert
        mockMvc.perform(put("/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("New Title")))
                .andExpect(jsonPath("$.year", is(2025)))
                .andExpect(jsonPath("$.genre", is("New Genre")));
    }

    @Test
    @DisplayName("Удаление книги — успешный сценарий")
    void deleteBookSuccess() throws Exception {
        // Arrange
        CreateBookDto createDto = CreateBookDto.builder()
                .title("To be deleted")
                .authorId(authorId)
                .year(2010)
                .genre("Mystery")
                .build();

        String bookJson = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long bookId = objectMapper.readTree(bookJson).get("id").asLong();

        // Act
        mockMvc.perform(delete("/books/" + bookId))
                .andExpect(status().isNoContent());

        // Assert
        mockMvc.perform(get("/books/" + bookId))
                .andExpect(status().isNotFound());
    }
}

