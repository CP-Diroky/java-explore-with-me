package ru.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query(value = """
            SELECT *
            FROM compilations
            WHERE pinned = ?1
            LIMIT ?2 OFFSET ?3
            """, nativeQuery = true)
    List<Compilation> getCompilations(boolean pinned, int size, int from);
}
