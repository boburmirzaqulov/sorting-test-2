package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.ValidatorDto;
import uz.springgroup.sortingtest2.entity.Mark;
import uz.springgroup.sortingtest2.entity.Student;
import uz.springgroup.sortingtest2.exception.DatabaseException;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.repository.MarkRepository;
import uz.springgroup.sortingtest2.repository.StudentRepository;
import uz.springgroup.sortingtest2.service.MarkService;
import uz.springgroup.sortingtest2.service.ValidationService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarkServiceImpl implements MarkService {
    private final MarkRepository markRepository;
    private final StudentRepository studentRepository;

    @Override
    public void setActiveAll(boolean b, List<Integer> studentIds) {
        if (!studentIds.isEmpty()) {
            try {
                List<Mark> marks = markRepository.findAllByStudentIdIn(studentIds);
                for (Mark mark : marks) {
                    mark.setActive(b);
                }
                markRepository.saveAll(marks);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DatabaseException(e.getMessage(), e);
            }
        }
    }

    @Override
    public ResponseDto<List<Mark>> saveAllWithStudentId(List<Mark> markList, Integer studentId) {
        /**
         * V A L I D A T I O N
         */
        List<ValidatorDto> errors = new ArrayList<>();
        for (Mark mark : markList) {
            errors.addAll(ValidationService.validationMark(mark));
        }
        if (!errors.isEmpty()) return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null, errors);
        try {
            if (!studentRepository.existsByIdAndIsActiveTrue(studentId)) return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseDto<>(false, AppCode.DATABASE_ERROR, AppMessages.DATABASE_ERROR, null);
        }

        /**
         * S A V I N G
         */
        try {
            markList = markList.stream()
                    .filter(e -> e.getSubject() != null)
                    .peek(e -> {
                        e.setId(null);
                        e.setStudent(new Student(studentId));
                    })
                    .collect(Collectors.toList());
            markList = markRepository.saveAll(markList);
            for (Mark mark : markList) {
                mark.setStudent(null);
            }
            return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, markList);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseDto<>(false, AppCode.DATABASE_ERROR, AppMessages.DATABASE_ERROR, null);
        }
    }
}
