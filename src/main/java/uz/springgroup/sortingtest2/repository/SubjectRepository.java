package uz.springgroup.sortingtest2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.springgroup.sortingtest2.entity.Subject;
import uz.springgroup.sortingtest2.entity.SubjectSt;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
//    @Query(value = "select sb.id, sb.name from subject sb join mark m on m.subject_id = sb.id join student s on m.student_id = s.id where s.id = ?1", nativeQuery = true)
//    List<SubjectSt> subjectSt(Integer id);
}
