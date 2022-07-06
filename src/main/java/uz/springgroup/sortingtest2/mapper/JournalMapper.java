package uz.springgroup.sortingtest2.mapper;

import org.mapstruct.Mapper;
import uz.springgroup.sortingtest2.dto.JournalDto;
import uz.springgroup.sortingtest2.entity.Journal;

@Mapper(componentModel = "spring")
public interface JournalMapper {
    Journal toEntity(JournalDto journalDto);

    JournalDto toDto(Journal journal);
}
