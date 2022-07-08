package uz.springgroup.sortingtest2.service;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import uz.springgroup.sortingtest2.dto.FacultyDto;
import uz.springgroup.sortingtest2.dto.UniversityDto;
import uz.springgroup.sortingtest2.dto.ValidatorDto;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.helper.StringHelper;
import uz.springgroup.sortingtest2.mapper.FacultyMapper;
import uz.springgroup.sortingtest2.repository.FacultyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ValidationService {

    public static void idValid(Integer id, List<ValidatorDto> errors){
        if (id == null) {
            errors.add(new ValidatorDto("id", AppMessages.EMPTY_FIELD));
            return;
        }
        if (id < 0) errors.add(new ValidatorDto("id", AppMessages.INCORRECT_TYPE));
    }

    public static void getAllGeneral(MultiValueMap<String, String> params, List<ValidatorDto> errors){
        boolean isPage = StringHelper.isNumber(params.getFirst("page"));
        boolean isSize = StringHelper.isNumber(params.getFirst("size"));
        if (!isPage) errors.add(new ValidatorDto("page", AppMessages.NOT_FOUND));
        if (!isSize) errors.add(new ValidatorDto("size", AppMessages.NOT_FOUND));
    }

    public static void validationFaculty(UniversityDto universityDto, List<ValidatorDto> errors, FacultyRepository facultyRepository, FacultyMapper facultyMapper) {
        if (universityDto.getFaculties() != null) {
            List<FacultyDto> facultyDtos = new ArrayList<>();
            for (int i = 0; i < universityDto.getFaculties().size(); i++) {
                FacultyDto facultyDto = universityDto.getFaculties().get(i);
                if (facultyDto.getId() != null) facultyDtos.add(facultyDto);

                UniversityDto universityDto1 = facultyDto.getUniversity();
                if (universityDto1 != null && universityDto1.getId() != null) {
                    errors.add(new ValidatorDto(
                            "Faculty with University ID = " + facultyDto.getUniversity().getId(),
                            AppMessages.INCORRECT_TYPE));
                }
            }

            if (!facultyDtos.isEmpty()) {
                List<FacultyDto> facultiesDB = facultyRepository.findAllById(
                                facultyDtos.stream()
                                        .map(FacultyDto::getId)
                                        .collect(Collectors.toList()))
                        .stream()
                        .map(facultyMapper::toDto)
                        .collect(Collectors.toList());

                if (facultiesDB.size() != facultyDtos.size()) {
                    List<FacultyDto> facultiesNotFound = facultyDtos.stream()
                            .filter(e -> !facultiesDB.contains(e))
                            .collect(Collectors.toList());

                    for (FacultyDto facultyDto : facultiesNotFound) {
                        errors.add(new ValidatorDto(
                                String.format("Faculty with ID = %d", facultyDto.getId()),
                                AppMessages.NOT_FOUND));
                    }
                }
            }
        }
    }

}
