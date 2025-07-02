package neket.bookaccounting.bookaccountingservice.mapper;

import neket.bookaccounting.bookaccountingservice.controller.dto.author.AuthorDto;
import neket.bookaccounting.bookaccountingservice.controller.dto.author.CreateAuthorDto;
import neket.bookaccounting.bookaccountingservice.entity.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper extends BaseMapper<Author, AuthorDto> {

    Author toEntity(CreateAuthorDto dto);
}
