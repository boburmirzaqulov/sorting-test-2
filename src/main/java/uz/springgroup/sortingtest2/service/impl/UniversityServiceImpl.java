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
import uz.springgroup.sortingtest2.dto.UniversityDto;
import uz.springgroup.sortingtest2.dto.ValidatorDto;
import uz.springgroup.sortingtest2.entity.Faculty;
import uz.springgroup.sortingtest2.entity.University;
import uz.springgroup.sortingtest2.exception.DatabaseException;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.helper.StringHelper;
import uz.springgroup.sortingtest2.mapper.FacultyMapper;
import uz.springgroup.sortingtest2.mapper.UniversityMapper;
import uz.springgroup.sortingtest2.repository.FacultyRepository;
import uz.springgroup.sortingtest2.repository.UniversityRepository;
import uz.springgroup.sortingtest2.service.UniversityService;
import uz.springgroup.sortingtest2.service.ValidationService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {
    private final UniversityMapper universityMapper;
    private final UniversityRepository universityRepository;
    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;
    private final FacultyServiceImpl facultyService;

    @Override
    public ResponseDto<UniversityDto> save(UniversityDto universityDto) {
        universityDto.setId(null);
        University university = universityMapper.toEntity(universityDto);
        universityRepository.save(university);

        if (university.getFaculties() != null){
            ResponseDto<List<Faculty>> responseDto = facultyService.saveAllWithUniversityId(
                    university.getFaculties(),
                    university.getId()
            );
            if (responseDto.isSuccess()) {
                university.setFaculties(responseDto.getData());
            } else {
                return new ResponseDto<>(responseDto.isSuccess(), responseDto.getCode(), responseDto.getMessage(), null, responseDto.getErrors());
            }
        }

        return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, universityMapper.toDto(university));
    }

    @Override
    public ResponseDto<?> getAll(MultiValueMap<String, String> params) {
        List<ValidatorDto> errors = new ArrayList<>();

        // V A L I D A T I O N
        ValidationService.getAllGeneral(params, errors);

        if (errors.isEmpty()) {
            int page = StringHelper.getNumber(params.getFirst("page"));
            int size = StringHelper.getNumber(params.getFirst("size"));
            try {
                PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
                Page<University> universityPage = universityRepository.findAllByIsActiveTrue(pageRequest);

                List<UniversityDto> universityDtoList = universityPage.get()
                        .map(e -> {
                            List<Faculty> faculties = e.getFaculties();
                            faculties.sort(Comparator.comparingInt(Faculty::getId));
                            for (Faculty faculty : faculties) {
                                faculty.setGroups(null);
                            }
                            return universityMapper.toDto(e);
                        })
                        .collect(Collectors.toList());

                Page<UniversityDto> result = new PageImpl<>(universityDtoList, universityPage.getPageable(), universityPage.getTotalPages());
                return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, result);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DatabaseException(e.getMessage(), e);
            }
        }
        return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, params, errors);
    }

    @Override
    public ResponseDto<UniversityDto> getById(Integer id) {
        // V A L I D A T I O N
        List<ValidatorDto> errors = new ArrayList<>();
        ValidationService.idValid(id, errors);
        if (!errors.isEmpty()) {
            return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null, errors);
        }
        try {
            Optional<University> universityOptional = universityRepository.findByIdAndIsActiveTrue(id);
            if (universityOptional.isEmpty()) {
                return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
            }
            University university = universityOptional.get();
            List<Integer> facultyIds = university.getFaculties().stream()
                    .map(Faculty::getId)
                    .collect(Collectors.toList());

            List<Faculty> facultiesDB = facultyRepository.findAllByIdInAndIsActiveTrue(facultyIds);

            for (Faculty faculty : facultiesDB) {
                faculty.setGroups(null);
            }
            facultiesDB.sort(Comparator.comparingInt(Faculty::getId));
            university.setFaculties(facultiesDB);
            return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, universityMapper.toDto(university));
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public ResponseDto<?> update(UniversityDto universityDto) {
        // V A L I D A T I O N
        List<ValidatorDto> errors = new ArrayList<>();
        ValidationService.idValid(universityDto.getId(), errors);
        if (!errors.isEmpty())
            return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null);

        List<Faculty> facultiesDtos = new ArrayList<>();
        try {
            Optional<University> universityOptional = universityRepository.findByIdAndIsActiveTrue(universityDto.getId());

            if (universityOptional.isEmpty()) {
                return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, universityDto);
            }

            University university = universityOptional.get();
            List<FacultyDto> facultyDtos = universityDto.getFaculties();
            if (facultyDtos != null) {
                facultiesDtos.addAll(facultyDtos.stream()
                        .map(facultyMapper::toEntity)
                        .collect(Collectors.toList()));
            }
            List<Faculty> faculties = university.getFaculties();
            if (faculties != null && !faculties.isEmpty()) {
                faculties.removeAll(facultiesDtos);
                // SET ACTIVE TO FALSE
                for (Faculty faculty : faculties) {
                    facultyService.delete(faculty.getId());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }

        University university = universityMapper.toEntity(universityDto);
        try {
            university.setActive(true);
            universityRepository.save(university);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }

        // U P D A T I N G   F A C U L T I E S
        if (!facultiesDtos.isEmpty()) {
            List<Faculty> faculties = facultyService.updateWithUniversity(university, facultiesDtos);
            faculties.sort(Comparator.comparingInt(Faculty::getId));
            university.setFaculties(faculties);
        }
        return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, universityMapper.toDto(university));
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
            Optional<University> universityOptional = universityRepository.findByIdAndIsActiveTrue(id);
            if (universityOptional.isPresent()) {
                List<University> universities = new ArrayList<>();
                universities.add(universityOptional.get());
                setActive(false, universities);
                return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, id);
            } else {
                return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    @Override
    public ResponseDto<UniversityDto> recoveryById(Integer id) {
        // V A L I D A T I O N
        List<ValidatorDto> errors = new ArrayList<>();
        ValidationService.idValid(id, errors);
        if (!errors.isEmpty()) {
            return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null, errors);
        }
        try {
            Optional<University> universityOptional = universityRepository.findByIdAndIsActiveFalse(id);
            if (universityOptional.isPresent()) {
                List<University> universities = new ArrayList<>();
                universities.add(universityOptional.get());
                setActive(true, universities);
                universities.get(0).getFaculties().sort(Comparator.comparingInt(Faculty::getId));
                return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, universityMapper.toDto(universities.get(0)));
            }
            return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }
    }

    private void setActive(boolean b, List<University> universities) {
        List<Integer> universityIds = new ArrayList<>();
        for (University university : universities) {
            university.setActive(b);
            universityIds.add(university.getId());
        }
        facultyService.setActiveAll(b, universityIds);
        try {
            universityRepository.saveAll(universities);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException(e.getMessage(), e);
        }
    }
}
