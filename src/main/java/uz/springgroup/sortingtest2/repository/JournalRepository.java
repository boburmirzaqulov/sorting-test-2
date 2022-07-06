package uz.springgroup.sortingtest2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.springgroup.sortingtest2.entity.Journal;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Integer> {
}
