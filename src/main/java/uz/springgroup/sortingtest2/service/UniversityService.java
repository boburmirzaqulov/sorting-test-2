package uz.springgroup.sortingtest2.service;

import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.UniversityDto;

public interface UniversityService {

    ResponseDto<UniversityDto> save(UniversityDto universityDto);
}
