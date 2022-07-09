package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import uz.springgroup.sortingtest2.dto.FacultyDto;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.ValidatorDto;
import uz.springgroup.sortingtest2.entity.Faculty;
import uz.springgroup.sortingtest2.entity.Group;
import uz.springgroup.sortingtest2.entity.GroupSt;
import uz.springgroup.sortingtest2.entity.University;
import uz.springgroup.sortingtest2.exception.DatabaseException;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.helper.StringHelper;
import uz.springgroup.sortingtest2.mapper.FacultyMapper;
import uz.springgroup.sortingtest2.repository.FacultyRepository;
import uz.springgroup.sortingtest2.repository.GroupRepository;
import uz.springgroup.sortingtest2.repository.UniversityRepository;
import uz.springgroup.sortingtest2.service.FacultyService;
import uz.springgroup.sortingtest2.service.ValidationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;
    private final UniversityRepository universityRepository;
    private final GroupRepository groupRepository;
    private final FacultyMapper facultyMapper;
    private final GroupServiceImpl groupService;

    @Override
    public ResponseDto<FacultyDto> save(FacultyDto facultyDto) {
        /**
         * V A L I D A T I O N
         */
        List<ValidatorDto> errors = new ArrayList<>();
        ValidationService.validationFacultyDtoForSave(facultyDto, errors, universityRepository, groupRepository);
        if (!errors.isEmpty())
            return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, facultyDto, errors);

        /**
         * S A V I N G
         */
        facultyDto.setId(null);
        Faculty faculty = facultyMapper.toEntity(facultyDto);
        University university = faculty.getUniversity();
        if (university != null) {
            if (university.getId() == null) {
                try {
                    university.setActive(true);
                    universityRepository.save(university);
                    faculty.setUniversity(university);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new DatabaseException(e.getMessage(), e);
                }
            } else {
                Optional<University> universityOptional = universityRepository.findByIdAndIsActiveTrue(university.getId());
                if (universityOptional.isPresent()) {
                    University university1 = universityOptional.get();
                    university1.setFaculties(null);
                    faculty.setUniversity(university1);
                }
            }
        }
        faculty.setActive(true);
        facultyRepository.save(faculty);
        List<Group> groups = faculty.getGroups();
        if (groups != null) {
            for (Group group : groups) {
                group.setFaculty(faculty);
            }
            groupRepository.saveAll(groups);
            for (Group group : groups) {
                group.setFaculty(null);
            }
            faculty.setGroups(groups);
        }
        return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, facultyMapper.toDto(faculty));
    }

    @Override
    public ResponseDto<List<Faculty>> saveAllWithUniversityId(List<Faculty> faculties, Integer universityId) {
        /**
         * V A L I D A T I O N
         */
        List<ValidatorDto> errors = new ArrayList<>();
        for (Faculty faculty : faculties) {
            errors.addAll(ValidationService.validationFaculty(faculty));
        }
        if (!errors.isEmpty()) return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null, errors);
        try {
            if (!universityRepository.existsByIdAndIsActiveTrue(universityId)) return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseDto<>(false, AppCode.DATABASE_ERROR, AppMessages.DATABASE_ERROR, null);
        }

        /**
         * S A V I N G
         */
        try {
            for (Faculty faculty : faculties) {
                faculty.setId(null);
                faculty.setUniversity(new University(universityId));
            }
            faculties = facultyRepository.saveAll(faculties);
            for (Faculty faculty : faculties) {
                if (faculty.getGroups() != null) {
                    ResponseDto<List<Group>> responseDto = groupService.saveAllWithFacultyId(faculty.getGroups(), faculty.getId());
                    if (responseDto.isSuccess()) {
                        faculty.setGroups(responseDto.getData());
                    } else {
                        return new ResponseDto<>(responseDto.isSuccess(), responseDto.getCode(), responseDto.getMessage(), null, responseDto.getErrors());
                    }
                }
                faculty.setUniversity(null);
            }
            return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, faculties);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseDto<>(false, AppCode.DATABASE_ERROR, AppMessages.DATABASE_ERROR, null);
        }
    }

    @Override
    public ResponseDto<?> getAll(MultiValueMap<String, String> params) {
        /**
         * V A L I D A T I O N
         */
        List<ValidatorDto> errors = new ArrayList<>();
        ValidationService.getAllGeneral(params, errors);

        if (errors.isEmpty()) {
            int page = StringHelper.getNumber(params.getFirst("page"));
            int size = StringHelper.getNumber(params.getFirst("size"));
            try {
                PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
                Page<Faculty> facultyPage = facultyRepository.findAllByIsActiveTrue(pageRequest);

                List<FacultyDto> facultyDtos = facultyPage.get()
                        .map(facultyMapper::toDto)
                        .collect(Collectors.toList());

                Page<FacultyDto> result = new PageImpl<>(facultyDtos, facultyPage.getPageable(), facultyPage.getTotalPages());
                return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, result);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseDto<>(false, AppCode.DATABASE_ERROR, AppMessages.DATABASE_ERROR, null);
            }
        }
        return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, params, errors);
    }

    @Override
    public ResponseDto<FacultyDto> getById(Integer id) {
        List<ValidatorDto> errors = new ArrayList<>();
        ValidationService.idValid(id, errors);
        if (!errors.isEmpty()) {
            return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null, errors);
        }
        Optional<Faculty> facultyOptional = facultyRepository.findByIdAndIsActiveTrue(id);
        if (facultyOptional.isEmpty()) {
            return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
        }
        Faculty faculty = facultyOptional.get();
        return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, facultyMapper.toDto(faculty));
    }

    @Override
    public ResponseDto<?> update(FacultyDto facultyDto) {
        // V A L I D A T I O N
        List<ValidatorDto> errors = new ArrayList<>();
        ValidationService.idValid(facultyDto.getId(), errors);
        if (!errors.isEmpty()) {
            return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null, errors);
        }

        try {
            if (!facultyRepository.existsByIdAndIsActiveTrue(facultyDto.getId())) {
                return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, facultyDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }

        Faculty faculty = facultyMapper.toEntity(facultyDto);
        faculty.setActive(true);
        facultyRepository.save(faculty);
        return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, facultyMapper.toDto(faculty));

    }

    @Override
    public ResponseDto<Integer> delete(Integer id) {
        /**
         * V A L I D A T I O N
         */
        List<ValidatorDto> errors = new ArrayList<>();
        ValidationService.idValid(id, errors);
        if (!errors.isEmpty()) {
            return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null, errors);
        }

        try {
            Optional<Faculty> facultyOptional = facultyRepository.findByIdAndIsActiveTrue(id);
            if (facultyOptional.isPresent()) {
                Faculty faculty = facultyOptional.get();
                faculty.setActive(false);
                groupService.setActiveOne(false, id);
                try {
                    facultyRepository.save(faculty);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new DatabaseException(e.getMessage(), e);
                }
                return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, id);
            } else {
                return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(),e);
        }

    }

    @Transactional
    @Override
    public List<Faculty> updateWithUniversity(University university, List<Faculty> faculties) {
        for (Faculty faculty : faculties) {
            faculty.setActive(true);
            faculty.setUniversity(university);
        }
        try {
            facultyRepository.saveAll(faculties);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }
        for (Faculty faculty : faculties) {
            faculty.setUniversity(null);
        }
        return faculties;
    }

    @Override
    public ResponseDto<List<?>> getAllGroupsById(Integer id) {
        if (!facultyRepository.existsByIdAndIsActiveTrue(id)) {
            return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
        }
        List<GroupSt> groupSts = facultyRepository.groupSt(id);
        return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, groupSts);
    }

    @Override
    public void setActiveAll(boolean b, List<Integer> universityIds) {
        if (!universityIds.isEmpty()) {
            try {
                List<Faculty> faculties = facultyRepository.findAllByUniversityIdIn(universityIds);
                if (!faculties.isEmpty()) {
                    List<Integer> facultyIds = new ArrayList<>();
                    for (Faculty faculty : faculties) {
                        faculty.setActive(b);
                        facultyIds.add(faculty.getId());
                    }
                    groupService.setActiveAll(b, facultyIds);
                    facultyRepository.saveAll(faculties);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new DatabaseException(e.getMessage(), e);
            }
        }
    }

    @Override
    public ResponseDto<FacultyDto> recoveryById(Integer id) {
        try {
            Optional<Faculty> facultyOptional = facultyRepository.findByIdAndIsActiveFalse(id);
            if (facultyOptional.isPresent()) {
                Faculty faculty = facultyOptional.get();
                faculty.setActive(true);
                groupService.setActiveOne(true, id);
                try {
                    facultyRepository.save(faculty);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new DatabaseException(e.getMessage(), e);
                }
                return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, facultyMapper.toDto(faculty));
            } else {
                return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
            }
        } catch (Exception e){
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(),e);
        }
    }
}
