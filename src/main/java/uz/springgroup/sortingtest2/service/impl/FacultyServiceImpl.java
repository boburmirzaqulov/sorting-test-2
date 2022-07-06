package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import uz.springgroup.sortingtest2.dto.FacultyDto;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.ValidatorDto;
import uz.springgroup.sortingtest2.entity.Faculty;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.helper.StringHelper;
import uz.springgroup.sortingtest2.mapper.FacultyMapper;
import uz.springgroup.sortingtest2.repository.FacultyRepository;
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
    private final FacultyMapper facultyMapper;
    @Override
    public ResponseDto<FacultyDto> save(FacultyDto facultyDto) {
        facultyDto.setId(null);
        Faculty faculty = facultyMapper.toEntity(facultyDto);
        facultyRepository.save(faculty);
        return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, facultyMapper.toDto(faculty));
    }

    @Override
    public ResponseDto<?> getAll(MultiValueMap<String, String> params) {
        List<ValidatorDto> errors = new ArrayList<>();
        boolean isPage = false, isSize=false;
        GeneralService.getAllGeneral(params, isPage, isSize, errors);
        if(isPage && isSize){
            int page = StringHelper.getNumber(params.getFirst("page"));
            int size = StringHelper.getNumber(params.getFirst("size"));
            try {
                PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
                Page<Faculty> facultyPage = facultyRepository.findAll(pageRequest);

                List<FacultyDto> facultyDtoList = facultyRepository.findAll()
                        .stream()
                        .map(facultyMapper::toDto)
                        .collect(Collectors.toList());
                Page<FacultyDto> result = new PageImpl<>(facultyDtoList, facultyPage.getPageable(), facultyPage.getTotalPages());
                return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, result);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseDto<>(false, AppCode.DATABASE_ERROR, AppMessages.DATABASE_ERROR,null);
            }
        }
        return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, params, errors);
    }

    @Override
    public ResponseDto<FacultyDto> getById(Integer id) {
        List<ValidatorDto> errors = new ArrayList<>();
        ValidationService.universityValid(id, errors);
        if (!errors.isEmpty()) {
            new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null, errors);
        }
        Optional<Faculty> facultyOptional = facultyRepository.findById(id);
        if (facultyOptional.isEmpty()) {
            return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
        }
        Faculty faculty = facultyOptional.get();
        return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, facultyMapper.toDto(faculty));
    }

    @Override
    public ResponseDto<?> update(FacultyDto facultyDto) {
        ResponseDto<?> responseDto = GeneralService.updateGeneral(facultyRepository, facultyDto.getId());
        if (responseDto == null) {
            Faculty faculty = facultyMapper.toEntity(facultyDto);
            facultyRepository.save(faculty);
            return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, facultyMapper.toDto(faculty));
        }
        return responseDto;
    }

    @Override
    public ResponseDto<Integer> delete(Integer id) {
        return GeneralService.deleteGeneral(facultyRepository, id);
    }
}