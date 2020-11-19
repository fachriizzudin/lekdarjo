package com.lazuardifachri.bps.lekdarjo.repository;

import com.lazuardifachri.bps.lekdarjo.model.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FileStorageRepository extends JpaRepository<FileModel,Long>  {
    @Query(value = "SELECT new FileModel(f.id, f.name, f.type, f.size) FROM FileModel f")
    List<FileModel> findAllInfo();
}
