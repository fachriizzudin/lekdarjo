package com.lazuardifachri.bps.lekdarjo.repository;

import com.lazuardifachri.bps.lekdarjo.model.Category;
import com.lazuardifachri.bps.lekdarjo.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findBySubject(Subject subject);
}
