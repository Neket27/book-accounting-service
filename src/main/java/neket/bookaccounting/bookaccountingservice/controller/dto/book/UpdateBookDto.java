package neket.bookaccounting.bookaccountingservice.controller.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.antlr.v4.runtime.misc.NotNull;

@Builder
public record UpdateBookDto(
        @NotBlank String title,
        @NotNull Long authorId,
        @Min(0) Integer year,
        String genre
) {}
