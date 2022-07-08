package uz.springgroup.sortingtest2.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import uz.springgroup.sortingtest2.dto.FacultyDto;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.UniversityDto;
import uz.springgroup.sortingtest2.service.impl.FacultyServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/faculty")
@RequiredArgsConstructor
public class FacultyResource {
    private final FacultyServiceImpl facultyService;

    @GetMapping("/{id}/groups")
    public ResponseDto<List<?>> getAllGroups(@PathVariable Integer id){
        return facultyService.getAllGroupsById(id);
    }

    @PostMapping("/{id}/recovery")
    public ResponseDto<FacultyDto> recoveryById(@PathVariable Integer id){
        return facultyService.recoveryById(id);
    }

    @PostMapping
    public ResponseDto<FacultyDto> save(@RequestBody @Valid FacultyDto facultyDto){
        return facultyService.save(facultyDto);
    }

    @GetMapping
    public ResponseDto<?> getAll(@RequestParam MultiValueMap<String, String> params){
        return facultyService.getAll(params);
    }

    @GetMapping("/{id}")
    public ResponseDto<FacultyDto> getById(@PathVariable Integer id){
        return facultyService.getById(id);
    }

    @PutMapping
    public ResponseDto<?> update(@RequestBody @Valid FacultyDto facultyDto){
        return facultyService.update(facultyDto);
    }

    @DeleteMapping("/{id}")
    public ResponseDto<Integer> delete(@PathVariable Integer id){
        return facultyService.delete(id);
    }
}
