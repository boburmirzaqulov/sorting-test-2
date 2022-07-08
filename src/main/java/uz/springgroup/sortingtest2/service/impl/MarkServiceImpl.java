package uz.springgroup.sortingtest2.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.springgroup.sortingtest2.entity.Mark;
import uz.springgroup.sortingtest2.repository.MarkRepository;
import uz.springgroup.sortingtest2.service.MarkService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarkServiceImpl implements MarkService {
    private final MarkRepository markRepository;

    @Override
    public boolean setActiveAll(boolean b, List<Integer> studentIds) {
        try {
            List<Mark> marks = markRepository.findAllByStudentIdIn(studentIds);
            for (Mark mark : marks) {
                mark.setActive(b);
            }
            markRepository.saveAll(marks);
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
