package com.example.demo.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.m_restaurant;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<m_restaurant, Integer> {
    Optional<m_restaurant> findByEmail(String email);
}
