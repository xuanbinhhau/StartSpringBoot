package com.example.demo.service;

import com.example.demo.repository.PermissionRepository;
import com.example.demo.dto.request.PermissionRequest;
import com.example.demo.dto.response.PermissionResponse;
import com.example.demo.entity.Permisssion;
import com.example.demo.maper.PermissionMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse createPermission(PermissionRequest request){
        Permisssion permisssion = permissionMapper.toPermission(request);
        return permissionMapper.toPermissionresponse(permissionRepository.save(permisssion));
    }

    public List<PermissionResponse> getAll(){
        var permission = permissionRepository.findAll();
        return permission.stream().map(permissionMapper::toPermissionresponse).toList();
    }
    public void deletePermision(String permissionName){
         permissionRepository.deleteById(permissionName);
    }
}
