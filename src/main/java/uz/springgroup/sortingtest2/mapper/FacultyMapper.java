package uz.springgroup.sortingtest2.mapper;

import org.mapstruct.Mapper;
import uz.springgroup.sortingtest2.dto.FacultyDto;
import uz.springgroup.sortingtest2.entity.Faculty;

@Mapper(componentModel = "spring")
public interface FacultyMapper {
    Faculty toEntity(FacultyDto facultyDto);

    FacultyDto toDto(Faculty faculty);
}
