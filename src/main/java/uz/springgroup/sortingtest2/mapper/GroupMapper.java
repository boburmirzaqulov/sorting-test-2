package uz.springgroup.sortingtest2.mapper;

import org.mapstruct.Mapper;
import uz.springgroup.sortingtest2.dto.GroupDto;
import uz.springgroup.sortingtest2.entity.Group;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    Group toEntity(GroupDto groupDto);

    GroupDto toDto(Group group);
}
