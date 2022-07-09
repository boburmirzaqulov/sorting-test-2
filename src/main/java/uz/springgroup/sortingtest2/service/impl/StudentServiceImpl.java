package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.StudentInfo;
import uz.springgroup.sortingtest2.dto.ValidatorDto;
import uz.springgroup.sortingtest2.entity.Group;
import uz.springgroup.sortingtest2.entity.Mark;
import uz.springgroup.sortingtest2.entity.Student;
import uz.springgroup.sortingtest2.entity.SubjectSt;
import uz.springgroup.sortingtest2.exception.DatabaseException;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.mapper.StudentMapper;
import uz.springgroup.sortingtest2.repository.GroupRepository;
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
    private final GroupRepository groupRepository;
    private final StudentMapper studentMapper;
    private final MarkServiceImpl markService;

    @Override
    public ResponseDto<List<SubjectSt>> subjectSt(Integer id) {
        // V A L I D A T I O N
        List<ValidatorDto> errors = new ArrayList<>();
        ValidationService.idValid(id, errors);
        if (!errors.isEmpty())
            return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null);

        if (!studentRepository.existsByIdAndIsActiveTrue(id)) {
            return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
        }

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

    @Override
    public void setActiveAll(boolean b, List<Integer> groupIds) {
        if (!groupIds.isEmpty()) {
            try {
                List<Student> students = studentRepository.findAllByGroupIdIn(groupIds);
                List<Integer> studentIds = new ArrayList<>();
                for (Student student : students) {
                    student.setActive(b);
                    studentIds.add(student.getId());
                }
                markService.setActiveAll(b, studentIds);
                studentRepository.saveAll(students);

            } catch (Exception e) {
                e.printStackTrace();
                throw new DatabaseException(e.getMessage(), e);
            }
        }
    }

    @Override
    public ResponseDto<List<Student>> saveAllWithGroupId(List<Student> students, Integer groupId) {
        /**
         * V A L I D A T I O N
         */
        List<ValidatorDto> errors = new ArrayList<>();
        for (Student student : students) {
            errors.addAll(ValidationService.validationStudent(student));
        }
        if (!errors.isEmpty()) return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null, errors);
        try {
            if (!groupRepository.existsByIdAndIsActiveTrue(groupId)) return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseDto<>(false, AppCode.DATABASE_ERROR, AppMessages.DATABASE_ERROR, null);
        }

        /**
         * S A V I N G
         */
        try {
            for (Student student : students) {
                student.setId(null);
                student.setGroup(new Group(groupId));
            }
            students = studentRepository.saveAll(students);

            for (Student student : students) {
                if (student.getMarkList() != null) {
                    ResponseDto<List<Mark>> responseDto = markService.saveAllWithStudentId(
                            student.getMarkList(),
                            student.getId()
                    );
                    if (responseDto.isSuccess()){
                        student.setMarkList(responseDto.getData());
                    } else {
                        return new ResponseDto<>(responseDto.isSuccess(), responseDto.getCode(), responseDto.getMessage(), null, responseDto.getErrors());
                    }
                }
                student.setGroup(null);
            }

            return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, students);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseDto<>(false, AppCode.DATABASE_ERROR, AppMessages.DATABASE_ERROR, null);
        }
    }
}
