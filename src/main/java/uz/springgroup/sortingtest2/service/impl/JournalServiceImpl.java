package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.springgroup.sortingtest2.entity.Journal;
import uz.springgroup.sortingtest2.repository.JournalRepository;
import uz.springgroup.sortingtest2.service.JournalService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalServiceImpl implements JournalService {
    private final JournalRepository journalRepository;
    private final SubjectServiceImpl subjectService;

    @Override
    public boolean setActiveAll(boolean b, List<Integer> groupIds) {
        try {
            List<Journal> journals = journalRepository.findAllByGroupIdIn(groupIds);
            for (Journal journal : journals) {
                journal.setActive(b);
            }
            boolean subject = subjectService.setActiveAll(b, journals);
            if (subject){
                journalRepository.saveAll(journals);
            }
            return subject;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
