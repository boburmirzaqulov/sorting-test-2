package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.springgroup.sortingtest2.entity.Journal;
import uz.springgroup.sortingtest2.entity.Subject;
import uz.springgroup.sortingtest2.repository.SubjectRepository;
import uz.springgroup.sortingtest2.service.SubjectService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;

    @Override
    public boolean setActiveAll(boolean b, List<Journal> journals) {
        try {
            List<Subject> subjects = subjectRepository.findAllByJournals(journals);
            for (Subject subject : subjects) {
                subject.setActive(b);
            }
            subjectRepository.saveAll(subjects);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
