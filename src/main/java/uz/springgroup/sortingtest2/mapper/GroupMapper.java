package uz.springgroup.sortingtest2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import uz.springgroup.sortingtest2.dto.*;
import uz.springgroup.sortingtest2.entity.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    @Mapping(target = "faculty", source = "groupDto.faculty", qualifiedByName = "toFacultyToEntity")
    @Mapping(target = "students", source = "groupDto.students", qualifiedByName = "toStudentsToEntity")
    @Mapping(target = "journal", source = "groupDto.journal", qualifiedByName = "toJournalToEntity")
    Group toEntity(GroupDto groupDto);

    @Named("toFacultyToEntity")
    default Faculty toFacultyToEntity(FacultyDto faculty){
        if (faculty == null) return null;
        faculty.setGroups(null);
        return FacultyMapper.INSTANCE.toEntity(faculty);
    }

    @Named("toStudentsToEntity")
    default List<Student> toStudentsToEntity(List<StudentDto> students){
        if (students == null) return null;
        return students.stream()
                .map(e -> {
                    e.setGroup(null);
                    return StudentMapper.INSTANCE.toEntity(e);
                })
                .collect(Collectors.toList());
    }

    @Named("toJournalToEntity")
    default Journal toJournalToEntity(JournalDto journal){
        if (journal == null) return null;
        journal.setGroup(null);
        return JournalMapper.INSTANCE.toEntity(journal);
    }

    @Mapping(target = "faculty", source = "group.faculty", qualifiedByName = "toFacultyToDto")
    @Mapping(target = "students", source = "group.students", qualifiedByName = "toStudentsToDto")
    @Mapping(target = "journal", source = "group.journal", qualifiedByName = "toJournalToDto")
    GroupDto toDto(Group group);

    @Named("toFacultyToDto")
    default FacultyDto toFacultyToDto(Faculty faculty){
        if (faculty == null) return null;
        faculty.setGroups(null);
        return FacultyMapper.INSTANCE.toDto(faculty);
    }

    @Named("toStudentsToDto")
    default List<StudentDto> toStudentsToDto(List<Student> students){
        if (students == null) return null;
        return students.stream()
                .map(e -> {
                    e.setGroup(null);
                    return StudentMapper.INSTANCE.toDto(e);
                })
                .collect(Collectors.toList());
    }

    @Named("toJournalToDto")
    default JournalDto toJournalToDto(Journal journal){
        if (journal == null) return null;
        journal.setGroup(null);
        return JournalMapper.INSTANCE.toDto(journal);
    }
}
