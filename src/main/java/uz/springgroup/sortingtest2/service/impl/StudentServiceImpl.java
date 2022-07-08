package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.StudentInfo;
import uz.springgroup.sortingtest2.dto.ValidatorDto;
import uz.springgroup.sortingtest2.entity.Student;
import uz.springgroup.sortingtest2.entity.SubjectSt;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.mapper.StudentMapper;
import uz.springgroup.sortingtest2.repository.StudentRepository;
import uz.springgroup.sortingtest2.repository.SubjectRepository;
import uz.springgroup.sortingtest2.service.StudentService;
import uz.springgroup.sortingtest2.service.ValidationService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final StudentMapper studentMapper;
    @Override
    public ResponseDto<List<SubjectSt>> subjectSt(Integer id) {
        // V A L I D A T I O N
        List<ValidatorDto> errors = new ArrayList<>();
        ValidationService.idValid(id, errors);
        if (!errors.isEmpty()) return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null);

        List<SubjectSt> subjects = studentRepository.subjectSt(id);

        return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, subjects);
    }

    @Override
    public ResponseDto<StudentInfo> findByName(String name) {
        Optional<Student> optionalStudent = studentRepository.findByName(name);
        if (optionalStudent.isEmpty()) {
            return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
        }
        Student student = optionalStudent.get();
        StudentInfo studentInfo = studentMapper.toInfo(student);
        List<SubjectSt> subjects = studentRepository.subjectSt(student.getId());
        Map<String, Double> subjectMap = new HashMap<>();

        for (SubjectSt subject : subjects) {
            subjectMap.put(subject.getName(), subject.getMark());
        }
        studentInfo.setSubjects(subjectMap);

        return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, studentInfo);
    }
}
