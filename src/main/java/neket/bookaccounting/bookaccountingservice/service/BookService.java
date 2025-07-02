package neket.bookaccounting.bookaccountingservice.service;

import neket.bookaccounting.bookaccountingservice.controller.dto.book.BookDto;
import neket.bookaccounting.bookaccountingservice.controller.dto.book.CreateBookDto;
import neket.bookaccounting.bookaccountingservice.controller.dto.book.UpdateBookDto;

import java.util.List;

public interface BookService {

    BookDto create(CreateBookDto dto);

    List<BookDto> list();

    BookDto get(Long id);

    BookDto update(Long id, UpdateBookDto dto);

    void delete(Long id);
}
