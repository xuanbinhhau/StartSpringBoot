package com.example.demo.service;

import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.maper.UserMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)

public class UserService {

     UserRepository userRepository;
     UserMapper userMapper;
     PasswordEncoder passwordEncoder;
     RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request) throws Exception {

        if (userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        var userRole = roleRepository.findById("USER").orElseThrow(
                () -> new AppException(ErrorCode.UN_AUTHENTICATED)
        );
        user.setRoles(new HashSet<>() {{
            add(userRole);
        }});

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('USER')")
    public List<UserResponse> getAllUser(){

        log.info("In method get Users");
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }
    public UserResponse updateUser(String userId, UserUpdateRequest request){

        User user = userRepository.findById(userId).orElseThrow(
                ()-> new RuntimeException("User Not Found")
        );
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        userMapper.updateUser(user,request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse getmyInfor(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(
                ()-> new AppException(ErrorCode.USER_NOT_EXITED)
        );

        return userMapper.toUserResponse(user);
    }


    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse findUserbyId(String id){
        log.info("In method getuser");
        return userMapper.toUserResponse(userRepository.findById(id).
                orElseThrow(()-> new RuntimeException("User Not Found")));
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }


}
