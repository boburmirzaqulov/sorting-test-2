package uz.springgroup.sortingtest2.service;

import org.springframework.util.MultiValueMap;
import uz.springgroup.sortingtest2.dto.FacultyDto;
import uz.springgroup.sortingtest2.dto.GroupDto;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.entity.Group;
import uz.springgroup.sortingtest2.entity.GroupSt;

import java.util.List;

public interface GroupService {
    ResponseDto<GroupDto> save(GroupDto groupDto);

    ResponseDto<?> getAll(MultiValueMap<String, String> params);

    ResponseDto<FacultyDto> getById(Integer id);

    ResponseDto<?> update(FacultyDto facultyDto);

    ResponseDto<Integer> delete(Integer id);

    ResponseDto<List<GroupSt>> getStudents(Integer id);

    void setActiveAll(boolean b, List<Integer> facultyIds);

    void setActiveOne(boolean b, Integer facultyId);
}
