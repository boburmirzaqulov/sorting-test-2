package uz.springgroup.sortingtest2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.springgroup.sortingtest2.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}
