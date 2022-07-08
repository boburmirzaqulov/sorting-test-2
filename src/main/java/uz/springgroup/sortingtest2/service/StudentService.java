package uz.springgroup.sortingtest2.service;

import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.StudentInfo;
import uz.springgroup.sortingtest2.entity.SubjectSt;

import java.util.List;

public interface StudentService {
    ResponseDto<List<SubjectSt>> subjectSt(Integer id);

    ResponseDto<StudentInfo> findByName(String name);
}
