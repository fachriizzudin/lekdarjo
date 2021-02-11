package com.lazuardifachri.bps.lekdarjo.repository;

import com.lazuardifachri.bps.lekdarjo.model.GraphMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GraphMetaRepository extends JpaRepository<GraphMeta, Long> {
    String QUERY = "SELECT new GraphMeta(g.id, g.title, g.subject, g.horizontal, g.vertical, g.verticalUnit, g.description) FROM GraphMeta g ";

    String ORDER = " ORDER by g.id ASC";

    @Query(QUERY + ORDER)
    Page<GraphMeta> findAll(Pageable pageable);

    @Query(QUERY + "WHERE g.id = :id")
    Optional<GraphMeta> findById(@Param("id") Long  id);
}
