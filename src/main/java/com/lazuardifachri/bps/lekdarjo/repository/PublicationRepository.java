package com.lazuardifachri.bps.lekdarjo.repository;

import com.lazuardifachri.bps.lekdarjo.model.EDistrict;
import com.lazuardifachri.bps.lekdarjo.model.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {

    String QUERY = "SELECT new Publication(p.id, p.title, p.catalogNo, p.publicationNo, p.issnOrIsbn, p.releaseDate, p.information, p.district, p.subject, p.imageUri, p.documentUri) FROM Publication p ";

    String ORDER = " ORDER by p.releaseDate DESC";

    @Query(QUERY + ORDER)
    Page<Publication> findAllInfo(Pageable pageable);

    @Query(QUERY + "WHERE p.id = :id")
    Optional<Publication> findByIdInfo(@Param("id") Long  id);

    String EDistrictPath = "com.lazuardifachri.bps.lekdarjo.model.";

    @Query(QUERY + "WHERE p.district = :code" + ORDER)
    Page<Publication> findByDistrict(@Param("code") EDistrict code, Pageable pageable);

    @Query(QUERY + "WHERE EXTRACT (year FROM p.releaseDate) = :year" + ORDER)
    Page<Publication> findByYear(@Param("year") Integer year, Pageable pageable);

    @Query(QUERY + "WHERE p.district = :code AND EXTRACT (year FROM p.releaseDate) = :year" + ORDER)
    Page<Publication> findByDistrictAndYear(@Param("code") EDistrict code, @Param("year") Integer year, Pageable pageable);

    @Query(QUERY + "WHERE p.subject.id = :subjectId")
    Page<Publication> findBySubject(@Param("subjectId") Long subjectId, Pageable pageable);

    @Query(QUERY + "WHERE p.subject.id = :subjectId AND EXTRACT (year FROM p.releaseDate) = :year")
    Page<Publication> findBySubjectAndYear(@Param("subjectId") Long subjectId, @Param("year") Integer year, Pageable pageable);

    Boolean existsByTitle(String title);
}
