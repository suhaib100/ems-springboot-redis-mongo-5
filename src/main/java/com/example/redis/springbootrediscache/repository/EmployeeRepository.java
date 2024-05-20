package com.example.redis.springbootrediscache.repository;

import com.example.redis.springbootrediscache.model.EmployeeMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends MongoRepository<EmployeeMongo,Integer> {
}
