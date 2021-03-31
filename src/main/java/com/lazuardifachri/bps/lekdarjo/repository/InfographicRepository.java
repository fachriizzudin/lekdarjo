package com.lazuardifachri.bps.lekdarjo.repository;

import com.lazuardifachri.bps.lekdarjo.model.Infographic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InfographicRepository extends JpaRepository<Infographic, Long> {
    String QUERY = "SELECT new Infographic(i.id, i.title, i.subject, i.releaseDate, i.imageUri) FROM Infographic i ";

    String ORDER = " ORDER by i.releaseDate DESC";

    String ORDERID = " ORDER by i.id DESC";

    @Query(QUERY + ORDER)
    Page<Infographic> findAllInfo(Pageable pageable);

    @Query(QUERY + "WHERE i.id = :id")
    Optional<Infographic> findByIdInfo(@Param("id") Long  id);

    @Query(QUERY + "WHERE i.subject.id = :subjectId" + ORDERID)
    Page<Infographic> findBySubject(@Param("subjectId") Long subjectId, Pageable pageable);

    Boolean existsByTitle(String title);
}
