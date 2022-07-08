package uz.springgroup.sortingtest2.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.StudentDto;
import uz.springgroup.sortingtest2.dto.StudentInfo;
import uz.springgroup.sortingtest2.entity.SubjectSt;
import uz.springgroup.sortingtest2.service.impl.StudentServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentResource {
    private final StudentServiceImpl studentService;

    @GetMapping("/{id}")
    public ResponseDto<List<SubjectSt>> getAllSubjects(@PathVariable Integer id){
        return studentService.subjectSt(id);
    }

    @GetMapping("/by-name")
    public ResponseDto<StudentInfo> getInfo(@RequestParam String name){
        return studentService.findByName(name);
    }
}
