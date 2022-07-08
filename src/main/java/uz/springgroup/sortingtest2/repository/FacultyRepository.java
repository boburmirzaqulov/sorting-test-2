package uz.springgroup.sortingtest2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.springgroup.sortingtest2.entity.Faculty;
import uz.springgroup.sortingtest2.entity.GroupSt;
import uz.springgroup.sortingtest2.entity.SubjectSt;
import uz.springgroup.sortingtest2.entity.University;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
    @Query(value = "select g.name, count(s.id) num from faculty f join groups g on g.faculty_id = f.id join student s on s.group_id = g.id where f.id = ?1 group by g.id", nativeQuery = true)
    List<GroupSt> groupSt(Integer id);

    List<Faculty> findAllByUniversityId(Integer universityId);

    List<Faculty> findAllByUniversityIdIn(List<Integer> universityIds);
}
