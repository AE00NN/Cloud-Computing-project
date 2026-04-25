package com.example.cloudproject.catalog.repository;

import com.example.cloudproject.catalog.domain.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepository extends JpaRepository<Editorial, Long> {

}
