package uz.springgroup.sortingtest2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.springgroup.sortingtest2.entity.Student;
import uz.springgroup.sortingtest2.entity.SubjectSt;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Query(value = "select sb.id, sb.name, avg(m.mark) mark from subject sb join mark m on m.subject_id = sb.id join student s on m.student_id = s.id where s.id = ?1 and s.is_active and sb.is_active and m.is_active group by sb.id", nativeQuery = true)
    List<SubjectSt> subjectSt(Integer id);

    Optional<Student> findByName(String name);

    List<Student> findAllByGroupIdIn(List<Integer> groupIds);

    Page<Student> findAllByIsActiveTrue(Pageable pageable);

    Optional<Student> findByIdAndIsActiveTrue(Integer id);

    Optional<Student> findByIdAndIsActiveFalse(Integer id);

    boolean existsByIdAndIsActiveTrue(Integer id);
}
