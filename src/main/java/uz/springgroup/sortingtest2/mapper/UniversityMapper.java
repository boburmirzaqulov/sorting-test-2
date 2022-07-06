package uz.springgroup.sortingtest2.mapper;

import org.mapstruct.Mapper;
import uz.springgroup.sortingtest2.dto.UniversityDto;
import uz.springgroup.sortingtest2.entity.University;

@Mapper(componentModel = "spring")
public interface UniversityMapper {
    University toEntity(UniversityDto universityDto);

    UniversityDto toDto(University university);
}
