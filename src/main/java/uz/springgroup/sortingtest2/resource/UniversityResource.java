package uz.springgroup.sortingtest2.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping
    public ResponseDto<?> getAll(@RequestParam MultiValueMap<String, String> params){
        return universityService.getAll(params);
    }

    @GetMapping("/{id}")
    public ResponseDto<UniversityDto> getById(@PathVariable Integer id){
        return universityService.getById(id);
    }

    @PutMapping
    public ResponseDto<UniversityDto> update(@RequestBody @Valid UniversityDto universityDto){
        return universityService.update(universityDto);
    }

    @DeleteMapping("/{id}")
    public ResponseDto<Integer> delete(@PathVariable Integer id){
        return universityService.delete(id);
    }
}
