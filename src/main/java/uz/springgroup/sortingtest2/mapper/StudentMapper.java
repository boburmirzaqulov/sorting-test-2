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
public interface StudentMapper {
    StudentMapper INSTANCE = Mappers.getMapper(StudentMapper.class);

    Student toEntity(StudentDto studentDto);

    @Mapping(target = "group", source = "student.group", qualifiedByName = "toGroupToDto")
    @Mapping(target = "markList", source = "student.markList", qualifiedByName = "toMarkListToDto")
    StudentDto toDto(Student student);

    @Named("toGroupToDto")
    default GroupDto toGroupToDto(Group group){
        if (group == null) return null;
        group.setStudents(null);
        return GroupMapper.INSTANCE.toDto(group);
    }

    @Named("toMarkListToDto")
    default List<MarkDto> toMarkListToDto(List<Mark> markList){
        if (markList == null) return null;
        return markList.stream()
                .map(e -> {
                    e.setStudent(null);
                    return MarkMapper.INSTANCE.toDto(e);
                })
                .collect(Collectors.toList());
    }

    @Mapping(target = "groupName", source = "student.group", qualifiedByName = "toGroup")
    @Mapping(target = "facultyName", source = "student.group.faculty", qualifiedByName = "toFaculty")
    @Mapping(target = "journalName", source = "student.group.journal", qualifiedByName = "toJournal")
    @Mapping(target = "universityName", source = "student.group.faculty.university", qualifiedByName = "toUniversity")
    StudentInfo toInfo(Student student);

    @Named("toGroup")
    default String toGroup(Group group){
        if (group == null) return null;
        return group.getName();
    }

    @Named("toFaculty")
    default String toFaculty(Faculty faculty){
        if (faculty == null) return null;
        return faculty.getName();
    }

    @Named("toJournal")
    default String toJournal(Journal journal){
        if (journal == null) return null;
        return journal.getName();
    }

    @Named("toUniversity")
    default String toUniversity(University university){
        if (university == null) return null;
        return university.getName();
    }

}
