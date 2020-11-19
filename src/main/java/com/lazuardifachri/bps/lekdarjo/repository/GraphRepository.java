package com.lazuardifachri.bps.lekdarjo.repository;

import com.lazuardifachri.bps.lekdarjo.model.Graph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GraphRepository extends JpaRepository<Graph, Long> {

    @Query("SELECT new Graph(g.id, g.value, g.year) FROM Graph g WHERE g.meta.id = :id")
    List<Graph> findAllByGraphMeta(@Param("id") Long id);

}
