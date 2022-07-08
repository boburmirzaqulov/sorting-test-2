package uz.springgroup.sortingtest2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import uz.springgroup.sortingtest2.dto.FacultyDto;
import uz.springgroup.sortingtest2.dto.UniversityDto;
import uz.springgroup.sortingtest2.entity.Faculty;
import uz.springgroup.sortingtest2.entity.University;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UniversityMapper {
    UniversityMapper INSTANCE = Mappers.getMapper(UniversityMapper.class);

    @Mapping(target = "faculties", source = "universityDto.faculties", qualifiedByName = "toFaculties")
    University toEntity(UniversityDto universityDto);

    @Named("toFaculties")
    default List<Faculty> toFaculties(List<FacultyDto> facultyDtos){
        if (facultyDtos == null) return null;
        return facultyDtos.stream()
                .map(e -> {
                    e.setUniversity(null);
                    return FacultyMapper.INSTANCE.toEntity(e);
                })
                .collect(Collectors.toList());
    }

    @Mapping(target = "faculties", source = "university.faculties", qualifiedByName = "toFacultiesToDto")
    UniversityDto toDto(University university);

    @Named("toFacultiesToDto")
    default List<FacultyDto> toFacultiesToDto(List<Faculty> faculties){
        if (faculties == null) return null;
        return faculties.stream()
                .map(e -> {
                    e.setUniversity(null);
                    return FacultyMapper.INSTANCE.toDto(e);
                })
                .collect(Collectors.toList());
    }
}
