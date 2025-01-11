package com.edgerank.demo.repository;

import com.edgerank.demo.Entity.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
}
