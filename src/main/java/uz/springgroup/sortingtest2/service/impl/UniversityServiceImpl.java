package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.UniversityDto;
import uz.springgroup.sortingtest2.entity.University;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.mapper.UniversityMapper;
import uz.springgroup.sortingtest2.repository.UniversityRepository;
import uz.springgroup.sortingtest2.service.UniversityService;

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
}
