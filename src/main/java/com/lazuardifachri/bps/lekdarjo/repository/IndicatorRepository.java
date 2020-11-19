package com.lazuardifachri.bps.lekdarjo.repository;

import com.lazuardifachri.bps.lekdarjo.model.Indicator;
import com.lazuardifachri.bps.lekdarjo.model.Indicator;
import com.lazuardifachri.bps.lekdarjo.model.StatisticalNews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IndicatorRepository extends JpaRepository<Indicator, Long> {

    String QUERY = "SELECT new Indicator(i.id, i.title, i.releaseDate, i.category, i.statType, i.documentUri) FROM Indicator i ";

    String ORDER = " ORDER by i.releaseDate DESC";

    @Query(QUERY + ORDER)
    Page<Indicator> findAllInfo(Pageable pageable);

    @Query(QUERY + "WHERE i.id = :id")
    Optional<Indicator> findByIdInfo(@Param("id") Long  id);

    @Query(QUERY + "WHERE i.category.id = :categoryId" + ORDER)
    Page<Indicator> findByCategory(@Param("categoryId") Long categoryid, Pageable pageable);

    Boolean existsByTitle(String title);
}
