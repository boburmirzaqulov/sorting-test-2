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
        // V A L I D A T I O N
        List<ValidatorDto> errors = new ArrayList<>();
        ValidationService.validationFaculty(universityDto, errors, facultyRepository, facultyMapper);
        if (!errors.isEmpty()) return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, universityDto, errors);

        universityDto.setId(null);
        University university = universityMapper.toEntity(universityDto);
        universityRepository.save(university);

        // S A V E    F A C U L T I E S
        university.setFaculties(facultyService.saveAll(university, universityDto.getFaculties()));

        return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, universityMapper.toDto(university));
    }

    @Override
    public ResponseDto<?> getAll(MultiValueMap<String, String> params) {
        List<ValidatorDto> errors = new ArrayList<>();

        // V A L I D A T I O N
        ValidationService.getAllGeneral(params, errors);

        if(errors.isEmpty()){
            int page = StringHelper.getNumber(params.getFirst("page"));
            int size = StringHelper.getNumber(params.getFirst("size"));
            try {
                PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
                Page<University> universityPage = universityRepository.findAll(pageRequest);

                List<UniversityDto> universityDtoList = universityRepository.findAll()
                        .stream()
                        .map(e -> {
                            for (Faculty faculty : e.getFaculties()) {
                                faculty.setGroups(null);
                            }
                            return universityMapper.toDto(e);
                        })
                        .collect(Collectors.toList());
                Page<UniversityDto> result = new PageImpl<>(universityDtoList, universityPage.getPageable(), universityPage.getTotalPages());
                return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, result);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseDto<>(false, AppCode.DATABASE_ERROR, AppMessages.DATABASE_ERROR,null);
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
            new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null, errors);
        }

        Optional<University> universityOptional = universityRepository.findById(id);
        if (universityOptional.isEmpty()) {
            return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
        }
        University university = universityOptional.get();
        for (Faculty faculty : university.getFaculties()) {
            faculty.setGroups(null);
        }
        return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, universityMapper.toDto(university));
    }

    @Transactional
    @Override
    public ResponseDto<?> update(UniversityDto universityDto) {
        // W I T H   V A L I D A T I O N
        ResponseDto<?> responseDto = GeneralService.updateGeneral(universityRepository, universityDto.getId());

        if (responseDto == null) {
            List<Faculty> faculties = universityDto.getFaculties().stream()
                    .map(facultyMapper::toEntity)
                    .collect(Collectors.toList());

            universityDto.setFaculties(null);
            University university = universityMapper.toEntity(universityDto);
            try {
                universityRepository.save(university);
            } catch (Exception e){
                e.printStackTrace();
                throw new DatabaseException(e.getMessage(), e);
            }
            university.setFaculties(facultyService.updateWithUniversity(university, faculties));
            return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, universityMapper.toDto(university));
        }
        return responseDto;
    }

    @Override
    public ResponseDto<Integer> delete(Integer id) {
        // W I T H   V A L I D A T I O N
        ResponseDto<Integer> res = GeneralService.deleteGeneral(universityRepository, id);

        if (res == null){
            Optional<University> universityOptional = universityRepository.findById(id);
            if (universityOptional.isPresent()) {
                University university = universityOptional.get();
                university.setActive(false);
                facultyService.setActiveOne(false, id);
                try {
                    universityRepository.save(university);
                } catch (Exception e){
                    e.printStackTrace();
                    throw new DatabaseException(e.getMessage(), e);
                }
                return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, id);
            }
        }
        return res;
    }
}
