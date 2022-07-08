package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.springgroup.sortingtest2.entity.Mark;
import uz.springgroup.sortingtest2.exception.DatabaseException;
import uz.springgroup.sortingtest2.repository.MarkRepository;
import uz.springgroup.sortingtest2.service.MarkService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkServiceImpl implements MarkService {
    private final MarkRepository markRepository;

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
}
