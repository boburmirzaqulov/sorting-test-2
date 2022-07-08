package uz.springgroup.sortingtest2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.springgroup.sortingtest2.entity.Group;
import uz.springgroup.sortingtest2.entity.GroupSt;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    @Query(value = "select s.name, avg(m.mark) num from student s inner join mark m on m.student_id = s.id inner join groups g on g.id = s.group_id where g.id = ?1 and s.is_active and m.is_active and g.is_active group by s.name order by num desc", nativeQuery = true)
    List<GroupSt> getInfoStudents(Integer id);

    List<Group> findAllByFacultyIdIn(List<Integer> facultyIds);

    List<Group> findAllByFacultyId(Integer facultyId);

    List<Group> findAllByIdInAndIsActiveTrue(List<Integer> groupsIds);

    Page<Group> findAllByIsActiveTrue(Pageable pageable);

    Optional<Group> findByIdAndIsActiveTrue(Integer id);

    Optional<Group> findByIdAndIsActiveFalse(Integer id);

    boolean existsByIdAndIsActiveTrue(Integer id);
}
