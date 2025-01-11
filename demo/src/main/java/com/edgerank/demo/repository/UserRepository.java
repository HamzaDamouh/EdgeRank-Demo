package com.edgerank.demo.repository;


import com.edgerank.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
