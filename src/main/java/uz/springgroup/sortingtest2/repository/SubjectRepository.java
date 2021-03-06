package uz.springgroup.sortingtest2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.springgroup.sortingtest2.entity.Subject;

import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    Page<Subject> findAllByIsActiveTrue(Pageable pageable);

    Optional<Subject> findByIdAndIsActiveTrue(Integer id);

    Optional<Subject> findByIdAndIsActiveFalse(Integer id);

    boolean existsByIdAndIsActiveTrue(Integer id);
}
