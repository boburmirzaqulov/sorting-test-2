package uz.springgroup.sortingtest2.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import uz.springgroup.sortingtest2.dto.FacultyDto;
import uz.springgroup.sortingtest2.dto.GroupDto;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.entity.GroupSt;
import uz.springgroup.sortingtest2.service.impl.GroupServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupResource {
    private final GroupServiceImpl groupService;

    @GetMapping("{id}/students")
    public ResponseDto<List<GroupSt>> getStudents(@PathVariable Integer id){
        return groupService.getStudents(id);
    }

    @PostMapping
    public ResponseDto<GroupDto> save(@RequestBody @Valid GroupDto groupDto){
        return groupService.save(groupDto);
    }

    @GetMapping
    public ResponseDto<?> getAll(@RequestParam MultiValueMap<String, String> params){
        return groupService.getAll(params);
    }

    @GetMapping("/{id}")
    public ResponseDto<FacultyDto> getById(@PathVariable Integer id){
        return groupService.getById(id);
    }

    @PutMapping
    public ResponseDto<?> update(@RequestBody @Valid FacultyDto facultyDto){
        return groupService.update(facultyDto);
    }

    @DeleteMapping("/{id}")
    public ResponseDto<Integer> delete(@PathVariable Integer id){
        return groupService.delete(id);
    }
}
