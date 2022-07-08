package uz.springgroup.sortingtest2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.springgroup.sortingtest2.entity.University;

@Repository
public interface UniversityRepository extends JpaRepository<University, Integer> {
    Page<University> findAllByIsActiveTrue(Pageable pageable);
}
