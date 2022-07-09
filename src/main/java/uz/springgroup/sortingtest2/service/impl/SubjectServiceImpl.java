package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.springgroup.sortingtest2.dto.ResponseDto;
import uz.springgroup.sortingtest2.entity.Group;
import uz.springgroup.sortingtest2.entity.Journal;
import uz.springgroup.sortingtest2.entity.Subject;
import uz.springgroup.sortingtest2.helper.AppCode;
import uz.springgroup.sortingtest2.helper.AppMessages;
import uz.springgroup.sortingtest2.repository.SubjectRepository;
import uz.springgroup.sortingtest2.service.SubjectService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;

    @Override
    public ResponseDto<List<Subject>> saveAllWithJournalId(List<Subject> subjects, Integer journalId) {
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
