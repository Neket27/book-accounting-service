package neket.bookaccounting.bookaccountingservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import neket.bookaccounting.bookaccountingservice.controller.dto.author.AuthorDto;
import neket.bookaccounting.bookaccountingservice.controller.dto.author.CreateAuthorDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ExtendWith(SpringExtension.class)
class AuthorControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Создание автора — успешный сценарий")
    void createAuthorSuccess() throws Exception {
        // Arrange
        CreateAuthorDto dto = CreateAuthorDto.builder()
                .name("Neket")
                .birthYear(40)
                .build();

        // Act & Assert
        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Neket")))
                .andExpect(jsonPath("$.birthYear", is(40)));
    }

    @Test
    @DisplayName("Создание автора — ошибка валидации")
    void createAuthorValidationFail() throws Exception {
        // Arrange
        CreateAuthorDto dto = CreateAuthorDto.builder().build();

        // Act & Assert
        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("name=must not be blank")));
    }

    @Test
    @DisplayName("Получение автора по ID — успешный сценарий")
    void getAuthorByIdSuccess() throws Exception {
        // Arrange
        CreateAuthorDto dto = CreateAuthorDto.builder().name("George Orwell").build();

        String json = mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andReturn().getResponse().getContentAsString();

        AuthorDto created = objectMapper.readValue(json, AuthorDto.class);

        // Act & Assert
        mockMvc.perform(get("/authors/" + created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("George Orwell")));
    }

    @Test
    @DisplayName("Получение автора по ID — автор не найден")
    void getAuthorByIdNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/authors/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Author with id: 999 not found.")));
    }

    @Test
    @DisplayName("Список авторов — с пагинацией")
    void listAuthors() throws Exception {
        // Arrange
        for (int i = 0; i < 55; i++) {
            CreateAuthorDto dto = CreateAuthorDto.builder().name("Author " + i).build();
            mockMvc.perform(post("/authors")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(dto)));
        }

        // Act & Assert
        mockMvc.perform(get("/authors?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)));

        mockMvc.perform(get("/authors?page=1&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(5))));
    }

    @Test
    @DisplayName("Список авторов — пустой результат")
    void listAuthorsEmpty() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/authors?page=100&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
