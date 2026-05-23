package com.example.demo.UserRepository;

import com.example.demo.entity.Permisssion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permisssion,String> {

}
