package uz.springgroup.sortingtest2.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.springgroup.sortingtest2.entity.Journal;
import uz.springgroup.sortingtest2.entity.University;

import java.util.List;
import java.util.Optional;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Integer> {
    List<Journal> findAllByGroupIdIn(List<Integer> groupIds);

    Page<Journal> findAllByIsActiveTrue(Pageable pageable);

    Optional<Journal> findByIdAndIsActiveTrue(Integer id);
}
