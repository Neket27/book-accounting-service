package neket.bookaccounting.bookaccountingservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import neket.bookaccounting.bookaccountingservice.controller.dto.author.AuthorDto;
import neket.bookaccounting.bookaccountingservice.controller.dto.author.CreateAuthorDto;
import neket.bookaccounting.bookaccountingservice.entity.Author;
import neket.bookaccounting.bookaccountingservice.exeption.NotFoundException;
import neket.bookaccounting.bookaccountingservice.mapper.AuthorMapper;
import neket.bookaccounting.bookaccountingservice.repository.AuthorRepository;
import neket.bookaccounting.bookaccountingservice.service.AuthorService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;
    private final AuthorMapper authorMapper;

    @Override
    @Transactional
    public AuthorDto create(CreateAuthorDto dto) {
        log.info("Creating author with name: {}", dto.name());
        Author author = authorMapper.toEntity(dto);
        Author saved = repository.save(author);
        log.info("Author created with id: {}", saved.getId());
        return authorMapper.toDto(saved);
    }

    @Override
    public List<AuthorDto> list(Pageable pageable) {
        log.debug("Fetching author list with page: {}", pageable);
        return authorMapper.toDtoList(repository.findAll(pageable).toList());
    }

    @Override
    public AuthorDto get(Long id) {
        log.debug("Fetching author with id: {}", id);
        return authorMapper.toDto(
                repository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Author with id: {0} not found.", id))
        );
    }
}
