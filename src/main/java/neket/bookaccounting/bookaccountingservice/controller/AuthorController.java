package neket.bookaccounting.bookaccountingservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import neket.bookaccounting.bookaccountingservice.controller.advice.annotation.CustomExceptionHandler;
import neket.bookaccounting.bookaccountingservice.controller.dto.author.AuthorDto;
import neket.bookaccounting.bookaccountingservice.controller.dto.author.CreateAuthorDto;
import neket.bookaccounting.bookaccountingservice.service.AuthorService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
@CustomExceptionHandler
public class AuthorController {

    private final AuthorService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorDto create(@RequestBody @Valid CreateAuthorDto dto) {
        return service.create(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AuthorDto> list(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        return service.list(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AuthorDto get(@PathVariable Long id) {
        return service.get(id);
    }
}
