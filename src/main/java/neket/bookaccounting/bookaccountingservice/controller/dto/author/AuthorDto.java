package neket.bookaccounting.bookaccountingservice.controller.dto.author;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public record AuthorDto(
        @NotNull Long id,
        @NotBlank String name,
        Integer birthYear
) {}