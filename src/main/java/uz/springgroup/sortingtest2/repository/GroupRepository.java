package uz.springgroup.sortingtest2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.springgroup.sortingtest2.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
}
