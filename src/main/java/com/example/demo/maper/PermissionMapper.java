package com.example.demo.maper;

import com.example.demo.dto.request.PermissionRequest;
import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.PermissionResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.Permisssion;
import com.example.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permisssion toPermission(PermissionRequest request);
    PermissionResponse toPermissionresponse(Permisssion permisssion);
}
