package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.springgroup.sortingtest2.entity.Journal;
import uz.springgroup.sortingtest2.exception.DatabaseException;
import uz.springgroup.sortingtest2.repository.JournalRepository;
import uz.springgroup.sortingtest2.service.JournalService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JournalServiceImpl implements JournalService {
    private final JournalRepository journalRepository;

    @Override
    public void setActiveAll(boolean b, List<Integer> groupIds) {
        if (!groupIds.isEmpty()) {
            try {
                List<Journal> journals = journalRepository.findAllByGroupIdIn(groupIds);
                for (Journal journal : journals) {
                    journal.setActive(b);
                }
                journalRepository.saveAll(journals);
            } catch (Exception e) {
                e.printStackTrace();
                throw new DatabaseException(e.getMessage(), e);
            }
        }
    }
}
