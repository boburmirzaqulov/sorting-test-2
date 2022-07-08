package uz.springgroup.sortingtest2.service;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import uz.springgroup.sortingtest2.dto.FacultyDto;
import uz.springgroup.sortingtest2.dto.GroupDto;
import uz.springgroup.sortingtest2.dto.UniversityDto;
import uz.springgroup.sortingtest2.dto.ValidatorDto;
import uz.springgroup.sortingtest2.entity.Group;
import uz.springgroup.sortingtest2.exception.DatabaseException;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.helper.StringHelper;
import uz.springgroup.sortingtest2.mapper.FacultyMapper;
import uz.springgroup.sortingtest2.repository.FacultyRepository;
import uz.springgroup.sortingtest2.repository.GroupRepository;
import uz.springgroup.sortingtest2.repository.UniversityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
                if (facultyDto.getId() != null) {
                    facultyDtos.add(facultyDto);
                }

                UniversityDto universityDto1 = facultyDto.getUniversity();
                if (universityDto1 != null) {
                    if (universityDto1.getId() != null) {
                        errors.add(new ValidatorDto(
                                "Faculty with University ID = " + facultyDto.getUniversity().getId(),
                                AppMessages.INCORRECT_TYPE));
                    }
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

    public static void validationFacultyDtoForSave(FacultyDto facultyDto, List<ValidatorDto> errors, UniversityRepository universityRepository, GroupRepository groupRepository) {
        UniversityDto universityDto = facultyDto.getUniversity();
        if (universityDto != null){
            if (universityDto.getId() != null){
                boolean uR;
                try {
                    uR = universityRepository.existsById(facultyDto.getUniversity().getId());
                } catch (Exception e){
                    e.printStackTrace();
                    throw new DatabaseException(e.getMessage(), e);
                }
                if (!uR) errors.add(new ValidatorDto("University ID", AppMessages.NOT_FOUND));
            }
        }

        List<GroupDto> groups = facultyDto.getGroups();
        if (groups != null){
            if (!groups.isEmpty()){
                List<Integer> groupIds = groups.stream()
                        .map(GroupDto::getId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .collect(Collectors.toList());
                List<Integer> groupIdsDB;
                try {
                    groupIdsDB = groupRepository.findAllByIdIn(groupIds).stream()
                            .map(Group::getId)
                            .distinct()
                            .collect(Collectors.toList());
                } catch (Exception e){
                    e.printStackTrace();
                    throw new DatabaseException(e.getMessage(), e);
                }
                if (groupIds.size() != groupIdsDB.size()){
                    groupIds.removeAll(groupIdsDB);
                    errors.add(
                            new ValidatorDto(String.format("Group Ids %s ", groupIds.toArray()), AppMessages.NOT_FOUND)
                    );
                }
            }
        }
    }
}
