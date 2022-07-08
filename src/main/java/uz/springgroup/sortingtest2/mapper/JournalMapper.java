package uz.springgroup.sortingtest2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import uz.springgroup.sortingtest2.dto.GroupDto;
import uz.springgroup.sortingtest2.dto.JournalDto;
import uz.springgroup.sortingtest2.dto.SubjectDto;
import uz.springgroup.sortingtest2.dto.UniversityDto;
import uz.springgroup.sortingtest2.entity.Group;
import uz.springgroup.sortingtest2.entity.Journal;
import uz.springgroup.sortingtest2.entity.Subject;
import uz.springgroup.sortingtest2.entity.University;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface JournalMapper {
    JournalMapper INSTANCE = Mappers.getMapper(JournalMapper.class);
    Journal toEntity(JournalDto journalDto);

    @Mapping(target = "group", source = "journal.group", qualifiedByName = "toGroupToDto")
    @Mapping(target = "subjects", source = "journal.subjects", qualifiedByName = "toSubjectsToDto")
    JournalDto toDto(Journal journal);

    @Named("toGroupToDto")
    default GroupDto toGroupToDto(Group group){
        if (group == null) return null;
        group.setJournal(null);
        return GroupMapper.INSTANCE.toDto(group);
    }

    @Named("toSubjectsToDto")
    default List<SubjectDto> toSubjectsToDto(List<Subject> subjects){
        if (subjects == null) return null;
        return subjects.stream()
                .map(e -> {
                    e.setJournals(null);
                    return SubjectMapper.INSTANCE.toDto(e);
                })
                .collect(Collectors.toList());
    }
}
