package uz.springgroup.sortingtest2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import uz.springgroup.sortingtest2.dto.FacultyDto;
import uz.springgroup.sortingtest2.dto.GroupDto;
import uz.springgroup.sortingtest2.dto.UniversityDto;
import uz.springgroup.sortingtest2.entity.Faculty;
import uz.springgroup.sortingtest2.entity.Group;
import uz.springgroup.sortingtest2.entity.University;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface FacultyMapper {
    FacultyMapper INSTANCE = Mappers.getMapper(FacultyMapper.class);

    @Mapping(target = "university", source = "facultyDto.university", qualifiedByName = "toUniversityToEntity")
    @Mapping(target = "groups", source = "facultyDto.groups", qualifiedByName = "toGroupsToEntity")
    Faculty toEntity(FacultyDto facultyDto);

    @Named("toUniversityToEntity")
    default University toUniversityToEntity(UniversityDto universityDto){
        if (universityDto == null) return null;
        universityDto.setFaculties(null);
        return UniversityMapper.INSTANCE.toEntity(universityDto);
    }

    @Named("toGroupsToEntity")
    default List<Group> toGroupsToEntity(List<GroupDto> groups){
        if (groups == null) return null;
        return groups.stream()
                .map(e -> {
                    e.setFaculty(null);
                    return GroupMapper.INSTANCE.toEntity(e);
                })
                .collect(Collectors.toList());
    }

    @Mapping(target = "university", source = "faculty.university", qualifiedByName = "toUniversityToDto")
    @Mapping(target = "groups", source = "faculty.groups", qualifiedByName = "toGroupsToDto")
    FacultyDto toDto(Faculty faculty);

    @Named("toUniversityToDto")
    default UniversityDto toUniversityToDto(University university){
        if (university == null) return null;
        university.setFaculties(null);
        return UniversityMapper.INSTANCE.toDto(university);
    }

    @Named("toGroupsToDto")
    default List<GroupDto> toGroupsToDto(List<Group> groups){
        if (groups == null) return null;
        return groups.stream()
                .map(e -> {
                    e.setFaculty(null);
                    return GroupMapper.INSTANCE.toDto(e);
                })
                .collect(Collectors.toList());
    }
}
