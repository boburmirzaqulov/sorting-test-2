package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.UniversityDto;
import uz.springgroup.sortingtest2.dto.ValidatorDto;
import uz.springgroup.sortingtest2.entity.University;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.helper.StringHelper;
import uz.springgroup.sortingtest2.mapper.UniversityMapper;
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

    @Override
    public ResponseDto<UniversityDto> save(UniversityDto universityDto) {
        universityDto.setId(null);
        University university = universityMapper.toEntity(universityDto);
        universityRepository.save(university);
        return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, universityMapper.toDto(university));
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
                Page<University> universityPage = universityRepository.findAll(pageRequest);

                List<UniversityDto> universityDtoList = universityRepository.findAll()
                        .stream()
                        .map(universityMapper::toDto)
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
        List<ValidatorDto> errors = new ArrayList<>();
        ValidationService.universityValid(id, errors);
        if (!errors.isEmpty()) {
            new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null, errors);
        }
        Optional<University> universityOptional = universityRepository.findById(id);
        if (universityOptional.isEmpty()) {
            return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
        }
        University university = universityOptional.get();
        return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, universityMapper.toDto(university));
    }

    @Override
    public ResponseDto<?> update(UniversityDto universityDto) {
        ResponseDto<?> responseDto = GeneralService.updateGeneral(universityRepository, universityDto.getId());
        if (responseDto == null) {
            University university = universityMapper.toEntity(universityDto);
            universityRepository.save(university);
            return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, universityMapper.toDto(university));
        }
        return responseDto;
    }

    @Override
    public ResponseDto<Integer> delete(Integer id) {
        return GeneralService.deleteGeneral(universityRepository, id);
    }
}
