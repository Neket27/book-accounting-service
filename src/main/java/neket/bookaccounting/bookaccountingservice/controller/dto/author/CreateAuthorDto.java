package neket.bookaccounting.bookaccountingservice.controller.dto.author;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
public record CreateAuthorDto(
        @NotBlank String name,
        Integer birthYear
) {}
