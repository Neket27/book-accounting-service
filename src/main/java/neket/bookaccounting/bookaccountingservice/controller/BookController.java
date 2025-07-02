package neket.bookaccounting.bookaccountingservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import neket.bookaccounting.bookaccountingservice.controller.advice.annotation.CustomExceptionHandler;
import neket.bookaccounting.bookaccountingservice.controller.dto.book.BookDto;
import neket.bookaccounting.bookaccountingservice.controller.dto.book.CreateBookDto;
import neket.bookaccounting.bookaccountingservice.controller.dto.book.UpdateBookDto;
import neket.bookaccounting.bookaccountingservice.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@CustomExceptionHandler
public class BookController {

    private final BookService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@RequestBody @Valid CreateBookDto dto) {
        return service.create(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookDto> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto update(@PathVariable Long id, @RequestBody @Valid UpdateBookDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
