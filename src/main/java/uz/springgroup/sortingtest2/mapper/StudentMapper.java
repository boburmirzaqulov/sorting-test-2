package uz.springgroup.sortingtest2.mapper;

import org.mapstruct.Mapper;
import uz.springgroup.sortingtest2.dto.StudentDto;
import uz.springgroup.sortingtest2.entity.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    Student toEntity(StudentDto studentDto);

    StudentDto toDto(Student student);
}
