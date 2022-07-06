package uz.springgroup.sortingtest2.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.UniversityDto;
import uz.springgroup.sortingtest2.service.impl.UniversityServiceImpl;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UniversityResource {
    private final UniversityServiceImpl universityService;

    @PostMapping
    public ResponseDto<UniversityDto> save(@RequestBody @Valid UniversityDto universityDto){
        return universityService.save(universityDto);
    }
}
