package uz.springgroup.sortingtest2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import uz.springgroup.sortingtest2.dto.GroupDto;
import uz.springgroup.sortingtest2.dto.MarkDto;
import uz.springgroup.sortingtest2.dto.StudentDto;
import uz.springgroup.sortingtest2.dto.SubjectDto;
import uz.springgroup.sortingtest2.entity.Group;
import uz.springgroup.sortingtest2.entity.Mark;
import uz.springgroup.sortingtest2.entity.Student;
import uz.springgroup.sortingtest2.entity.Subject;

@Mapper(componentModel = "spring")
public interface MarkMapper {
    MarkMapper INSTANCE = Mappers.getMapper(MarkMapper.class);

    Mark toEntity(MarkDto markDto);

    @Mapping(target = "student", source = "mark.student", qualifiedByName = "toStudentToDto")
    @Mapping(target = "subject", source = "mark.subject", qualifiedByName = "toSubjectToDto")
    MarkDto toDto(Mark mark);

    @Named("toStudentToDto")
    default StudentDto toStudentToDto(Student student){
        if (student == null) return null;
        student.setMarkList(null);
        return StudentMapper.INSTANCE.toDto(student);
    }

    @Named("toSubjectToDto")
    default SubjectDto toSubjectToDto(Subject subject){
        if (subject == null) return null;
        subject.setJournals(null);
        return SubjectMapper.INSTANCE.toDto(subject);
    }
}
