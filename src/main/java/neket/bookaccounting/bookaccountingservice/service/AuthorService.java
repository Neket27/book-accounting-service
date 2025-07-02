package neket.bookaccounting.bookaccountingservice.service;

import neket.bookaccounting.bookaccountingservice.controller.dto.author.AuthorDto;
import neket.bookaccounting.bookaccountingservice.controller.dto.author.CreateAuthorDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorService {
    AuthorDto create(CreateAuthorDto dto);

    List<AuthorDto> list(Pageable pageable);

    AuthorDto get(Long id);
}
