package com.example.teamproject1.repository;

import com.example.teamproject1.entity.Data;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface DataRepository extends CrudRepository<Data,Long> {
 ArrayList<Data> findAll();
}
