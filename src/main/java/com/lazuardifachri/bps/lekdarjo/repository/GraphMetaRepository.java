package com.lazuardifachri.bps.lekdarjo.repository;

import com.lazuardifachri.bps.lekdarjo.model.GraphMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface GraphMetaRepository extends JpaRepository<GraphMeta, Long> {
    String QUERY = "SELECT new GraphMeta(g.id, g.serialNumber, g.title, g.subject, g.horizontal, g.vertical, g.verticalUnit, g.description, g.graphType, g.dataType, g.imageUri) FROM GraphMeta g ";

    String ORDER = " ORDER by g.id ASC";

    @Query(QUERY + ORDER)
    List<GraphMeta> findAll();

    @Query("SELECT g.serialNumber FROM GraphMeta g")
    List<Integer> readAllSerialNumber();

    @Query(QUERY + "WHERE g.id = :id")
    Optional<GraphMeta> findById(@Param("id") Long  id);

    @Query(QUERY + "WHERE g.serialNumber = :serialNumber")
    Optional<GraphMeta> findBySerialNumber(@Param("serialNumber") Integer serialNumber);

    @Transactional
    @Modifying
    @Query("DELETE FROM GraphMeta g WHERE g.serialNumber = :serialNumber")
    void deleteBySerialNumber(@Param("serialNumber") Integer serialNumber);
}
