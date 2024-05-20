package com.example.redis.springbootrediscache.controller;


import com.example.redis.springbootrediscache.ResouceNotFoundException;
import com.example.redis.springbootrediscache.model.EmployeeMongo;
import com.example.redis.springbootrediscache.model.EmployeeRedis;
import com.example.redis.springbootrediscache.repository.EmployeeRedisRepository;
import com.example.redis.springbootrediscache.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {






    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeRedisRepository employeeRedisRepository;

    @PostMapping("/save")
    public ResponseEntity<String> saveEmployee(@RequestBody EmployeeMongo employee) {
        // Save to MongoDB
        employeeRepository.save(employee);

        // Save to Redis
        EmployeeRedis employeeRedis = convertToEmployeeRedis(employee);
        employeeRedisRepository.save(employeeRedis);

        return ResponseEntity.ok("Employee saved to MongoDB and Redis");
    }

    private EmployeeRedis convertToEmployeeRedis(EmployeeMongo employeeMongo) {
        EmployeeRedis employeeRedis = new EmployeeRedis();
        employeeRedis.setId(employeeMongo.getId());
        employeeRedis.setName(employeeMongo.getName());
        return employeeRedis;
    }

    @PostMapping("/employees")
    public EmployeeMongo addEmployee(@RequestBody EmployeeMongo employee) {

        return employeeRepository.save(employee);
    }


    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeMongo>> getAllEmployees() {
        return ResponseEntity.ok(employeeRepository.findAll());
    }

    @GetMapping("employees/{employeeId}")
    @Cacheable(value = "employees",key = "#employeeId")
    public EmployeeMongo findEmployeeById(@PathVariable(value = "employeeId") Integer employeeId) {
        System.out.println("Employee fetching from database:: "+employeeId);
        return employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResouceNotFoundException("Employee not found" + employeeId));

    }


    @PutMapping("employees/{employeeId}")
    @CachePut(value = "employees",key = "#employeeId")
    public EmployeeMongo updateEmployee(@PathVariable(value = "employeeId") Integer employeeId,
                                        @RequestBody EmployeeMongo employeeDetails) {
        EmployeeMongo employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResouceNotFoundException("Employee not found for this id :: " + employeeId));
        employee.setName(employeeDetails.getName());
        final EmployeeMongo updatedEmployee = employeeRepository.save(employee);

        EmployeeRedis employeeRedis = new EmployeeRedis(employeeId, employeeDetails.getName());
        employeeRedisRepository.save(employeeRedis);


        return updatedEmployee;

    }




    @DeleteMapping("employees/{id}")
    @CacheEvict(value = "employees", allEntries = true)
    public void deleteEmployee(@PathVariable(value = "id") Integer employeeId) {
        EmployeeMongo employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new ResouceNotFoundException("Employee not found" + employeeId));
        employeeRepository.delete(employee);
    }
}
