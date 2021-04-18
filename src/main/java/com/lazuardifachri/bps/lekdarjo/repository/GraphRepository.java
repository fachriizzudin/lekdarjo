package com.lazuardifachri.bps.lekdarjo.repository;

import com.lazuardifachri.bps.lekdarjo.model.Graph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GraphRepository extends JpaRepository<Graph, Long> {

    String ORDER = " ORDER by g.year ASC";

    @Query("SELECT new Graph(g.id, g.value, g.year) FROM Graph g WHERE g.meta.serialNumber = :serialNumber" + ORDER)
    List<Graph> findAllByGraphMeta(@Param("serialNumber") Integer serialNumber);

    @Query("DELETE FROM Graph g WHERE g.meta.serialNumber = :serialNumber")
    List<Graph> deleteAllByGraphMeta(@Param("serialNumber") Integer serialNumber);

    @Query("SELECT COUNT(*) FROM Graph g WHERE g.year = :year AND g.meta.serialNumber = :serialNumber")
    Long existByYear(@Param("serialNumber") Integer serialNumber, @Param("year") int year);

}
