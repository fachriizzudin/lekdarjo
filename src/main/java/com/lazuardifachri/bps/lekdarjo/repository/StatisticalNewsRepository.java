package com.lazuardifachri.bps.lekdarjo.repository;

import com.lazuardifachri.bps.lekdarjo.model.StatisticalNews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StatisticalNewsRepository extends JpaRepository<StatisticalNews,Long> {

    String QUERY = "SELECT new StatisticalNews(s.id, s.title, s.abstraction, s.releaseDate, s.category, s.documentUri) FROM StatisticalNews s ";

    String ORDER = " ORDER by s.releaseDate DESC";

    @Query(QUERY + ORDER)
    Page<StatisticalNews> findAllInfo(Pageable pageable);

    @Query(QUERY + "WHERE s.id = :id")
    Optional<StatisticalNews> findByIdInfo(@Param("id") Long id);

    @Query(QUERY + "WHERE s.category.id = :categoryId" + ORDER)
    Page<StatisticalNews> findByCategory(@Param("categoryId") Long categoryid, Pageable pageable);

    @Query(QUERY + "WHERE s.category.id = :categoryId AND EXTRACT (year FROM s.releaseDate) = :year" + ORDER)
    Page<StatisticalNews> findByCategoryAndYear(@Param("categoryId") Long categoryid, @Param("year") Integer year, Pageable pageable);

    @Query(QUERY + "WHERE s.category.id = :categoryId AND EXTRACT (year FROM s.releaseDate) = :year AND EXTRACT (month FROM s.releaseDate) = :month" + ORDER)
    Page<StatisticalNews> findByCategoryAndYearAndMonth(@Param("categoryId") Long categoryid, @Param("year") Integer year, @Param("month") Integer month, Pageable pageable);

    @Query(QUERY + "WHERE EXTRACT (year FROM s.releaseDate) = :year" + ORDER)
    Page<StatisticalNews> findByYear(@Param("year") Integer year, Pageable pageable);

    @Query(QUERY + "WHERE EXTRACT (year FROM s.releaseDate) = :year AND EXTRACT (month FROM s.releaseDate) = :month" + ORDER)
    Page<StatisticalNews> findByYearAndMonth(@Param("year") Integer year, @Param("month") Integer month, Pageable pageable);

    Boolean existsByTitle(String title);
}
