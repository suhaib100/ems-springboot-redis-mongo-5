package com.example.redis.springbootrediscache.repository;

import com.example.redis.springbootrediscache.model.EmployeeRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRedisRepository extends CrudRepository<EmployeeRedis, Integer> {
}
