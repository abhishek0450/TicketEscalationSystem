package com.ticketing.repository;

import com.ticketing.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// TODO: implemented in Phase X
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
