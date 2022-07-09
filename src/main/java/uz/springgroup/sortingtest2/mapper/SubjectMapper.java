package uz.springgroup.sortingtest2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import uz.springgroup.sortingtest2.dto.GroupDto;
import uz.springgroup.sortingtest2.dto.JournalDto;
import uz.springgroup.sortingtest2.dto.MarkDto;
import uz.springgroup.sortingtest2.dto.SubjectDto;
import uz.springgroup.sortingtest2.entity.Group;
import uz.springgroup.sortingtest2.entity.Journal;
import uz.springgroup.sortingtest2.entity.Mark;
import uz.springgroup.sortingtest2.entity.Subject;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    SubjectMapper INSTANCE = Mappers.getMapper(SubjectMapper.class);

    @Mapping(target = "journals", source = "subjectDto.journals", qualifiedByName = "toJournalsToEntity")
    @Mapping(target = "markList", source = "subjectDto.markList", qualifiedByName = "toMarkListToEntity")
    Subject toEntity(SubjectDto subjectDto);

    @Named("toJournalsToEntity")
    default List<Journal> toJournalsToEntity(List<JournalDto> journals){
        if (journals == null) return null;
        return journals.stream()
                .map(e -> {
                    e.setSubjects(null);
                    return JournalMapper.INSTANCE.toEntity(e);
                })
                .collect(Collectors.toList());
    }

    @Named("toMarkListToEntity")
    default List<Mark> toMarkListToEntity(List<MarkDto> markList){
        if (markList == null) return null;
        return markList.stream()
                .map(e -> {
                    e.setSubject(null);
                    return MarkMapper.INSTANCE.toEntity(e);
                })
                .collect(Collectors.toList());
    }

    @Mapping(target = "journals", source = "subject.journals", qualifiedByName = "toJournalsToDto")
    @Mapping(target = "markList", source = "subject.markList", qualifiedByName = "toMarkListToDto")
    SubjectDto toDto(Subject subject);

    @Named("toJournalsToDto")
    default List<JournalDto> toJournalsToDto(List<Journal> journals){
        if (journals == null) return null;
        return journals.stream()
                .map(e -> {
                    e.setSubjects(null);
                    return JournalMapper.INSTANCE.toDto(e);
                })
                .collect(Collectors.toList());
    }

    @Named("toMarkListToDto")
    default List<MarkDto> toMarkListToDto(List<Mark> markList){
        if (markList == null) return null;
        return markList.stream()
                .map(e -> {
                    e.setSubject(null);
                    return MarkMapper.INSTANCE.toDto(e);
                })
                .collect(Collectors.toList());
    }
}
