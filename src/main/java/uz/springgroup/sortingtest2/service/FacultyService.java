package uz.springgroup.sortingtest2.service;

import org.springframework.util.MultiValueMap;
import uz.springgroup.sortingtest2.dto.FacultyDto;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.UniversityDto;

public interface FacultyService {
    ResponseDto<FacultyDto> save(FacultyDto facultyDto);

    ResponseDto<?> getAll(MultiValueMap<String, String> params);

    ResponseDto<FacultyDto> getById(Integer id);

    ResponseDto<?> update(FacultyDto facultyDto);

    ResponseDto<Integer> delete(Integer id);
}
