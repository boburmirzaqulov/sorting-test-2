package uz.springgroup.sortingtest2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.springgroup.sortingtest2.entity.Mark;
import uz.springgroup.sortingtest2.entity.Student;
import uz.springgroup.sortingtest2.entity.Subject;

import java.util.List;

@Repository
public interface MarkRepository extends JpaRepository<Mark, Integer> {
}
