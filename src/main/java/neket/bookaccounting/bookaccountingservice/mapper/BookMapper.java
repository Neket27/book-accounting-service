package neket.bookaccounting.bookaccountingservice.mapper;

import neket.bookaccounting.bookaccountingservice.controller.dto.book.BookDto;
import neket.bookaccounting.bookaccountingservice.controller.dto.book.CreateBookDto;
import neket.bookaccounting.bookaccountingservice.controller.dto.book.UpdateBookDto;
import neket.bookaccounting.bookaccountingservice.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper extends BaseMapper<Book, BookDto> {

    Book toEntity(CreateBookDto dto);

    @Mapping(target = "authorId", source = "author.id")
    BookDto toDto(Book entity);

    void update(@MappingTarget Book entity, UpdateBookDto dto);
}
