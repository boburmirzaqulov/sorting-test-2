package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.dto.ValidatorDto;
import uz.springgroup.sortingtest2.entity.Group;
import uz.springgroup.sortingtest2.entity.Journal;
import uz.springgroup.sortingtest2.entity.Subject;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.repository.JournalRepository;
import uz.springgroup.sortingtest2.repository.SubjectRepository;
import uz.springgroup.sortingtest2.service.SubjectService;
import uz.springgroup.sortingtest2.service.ValidationService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final JournalRepository journalRepository;

    @Override
    public ResponseDto<List<Subject>> saveAllWithJournalId(List<Subject> subjects, Integer journalId) {
        /**
         * V A L I D A T I O N
         */
        List<ValidatorDto> errors = new ArrayList<>();
        for (Subject subject : subjects) {
            errors.addAll(ValidationService.validationSubject(subject));
        }
        if (!errors.isEmpty()) return new ResponseDto<>(false, AppCode.VALIDATOR_ERROR, AppMessages.VALIDATOR_MESSAGE, null, errors);
        try {
            if (!journalRepository.existsByIdAndIsActiveTrue(journalId)) return new ResponseDto<>(false, AppCode.NOT_FOUND, AppMessages.NOT_FOUND, null);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseDto<>(false, AppCode.DATABASE_ERROR, AppMessages.DATABASE_ERROR, null);
        }

        /**
         * S A V I N G
         */
        try {
            List<Journal> journals = new ArrayList<>();
            journals.add(new Journal(journalId));
            for (Subject subject : subjects) {
                subject.setId(null);
                subject.setJournals(journals);
            }
            subjectRepository.saveAll(subjects);
            for (Subject subject : subjects) {
                subject.setJournals(null);
            }
            return new ResponseDto<>(true, AppCode.OK, AppMessages.OK, subjects);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseDto<>(false, AppCode.DATABASE_ERROR, AppMessages.DATABASE_ERROR, null);
        }
    }
}
