package uz.springgroup.sortingtest2.mapper;

import org.mapstruct.Mapper;
import uz.springgroup.sortingtest2.dto.MarkDto;
import uz.springgroup.sortingtest2.entity.Mark;

@Mapper(componentModel = "spring")
public interface MarkMapper {
    Mark toEntity(MarkDto markDto);

    MarkDto toDto(Mark mark);
}
