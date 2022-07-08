package uz.springgroup.sortingtest2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.springgroup.sortingtest2.entity.Group;
import uz.springgroup.sortingtest2.entity.GroupSt;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    @Query(value = "select s.name, avg(m.mark) num from student s inner join mark m on m.student_id = s.id inner join groups g on g.id = s.group_id where g.id = ?1 group by s.name order by num desc", nativeQuery = true)
    List<GroupSt> getInfoStudents(Integer id);
}
