package com.example.teamproject1.repository;

import com.example.teamproject1.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface LoginRepository extends CrudRepository<User, Long> {
    @Override
    ArrayList<User> findAll();
}
