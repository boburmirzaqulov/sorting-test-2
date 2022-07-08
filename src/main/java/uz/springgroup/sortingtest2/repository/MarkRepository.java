package uz.springgroup.sortingtest2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.springgroup.sortingtest2.entity.Mark;
import uz.springgroup.sortingtest2.entity.University;

import java.util.List;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Integer> {
    List<Mark> findAllByStudentIdIn(List<Integer> studentIds);

    Page<Mark> findAllByIsActiveTrue(Pageable pageable);
}
