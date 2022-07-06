package uz.springgroup.sortingtest2.service;

import org.springframework.util.MultiValueMap;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.UniversityDto;

public interface UniversityService {

    ResponseDto<UniversityDto> save(UniversityDto universityDto);

    ResponseDto<?> getAll(MultiValueMap<String, String> params);

    ResponseDto<UniversityDto> getById(Integer id);

    ResponseDto<UniversityDto> update(UniversityDto universityDto);

    ResponseDto<Integer> delete(Integer id);
}
