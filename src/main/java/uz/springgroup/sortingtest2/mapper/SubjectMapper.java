package uz.springgroup.sortingtest2.mapper;

import org.mapstruct.Mapper;
import uz.springgroup.sortingtest2.dto.SubjectDto;
import uz.springgroup.sortingtest2.entity.Subject;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    Subject toEntity(SubjectDto subjectDto);

    SubjectDto toDto(Subject subject);
}
