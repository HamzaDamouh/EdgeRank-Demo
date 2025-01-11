package com.edgerank.demo.repository;

import com.edgerank.demo.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
