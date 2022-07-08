package uz.springgroup.sortingtest2.service;

import org.springframework.util.MultiValueMap;
import uz.springgroup.sortingtest2.dto.FacultyDto;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.entity.Faculty;
import uz.springgroup.sortingtest2.entity.University;

import java.util.List;

public interface FacultyService {
    ResponseDto<FacultyDto> save(FacultyDto facultyDto);

    ResponseDto<?> getAll(MultiValueMap<String, String> params);

    ResponseDto<FacultyDto> getById(Integer id);

    ResponseDto<?> update(FacultyDto facultyDto);

    ResponseDto<Integer> delete(Integer id);

    List<Faculty> saveAll(University university, List<FacultyDto> facultyDtos);

    List<Faculty> updateWithUniversity(University university, List<Faculty> faculties);

    ResponseDto<List<?>> getAllGroupsById(Integer id);

    void setActiveAll(boolean b, List<Integer> universityIds);
}
