package com.example.redis.springbootrediscache.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("Employee")
public class EmployeeMongo implements Serializable {

    @Id
    private int id;
    private String name;

}
