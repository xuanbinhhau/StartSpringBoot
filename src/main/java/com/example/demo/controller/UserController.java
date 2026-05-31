package com.example.demo.controller;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserController {

     UserService userService;

    @PostMapping()
    ApiResponse<UserResponse> createUser(@RequestBody @Validated UserCreationRequest request) throws Exception {
        log.info("Controller create user");
        ApiResponse<UserResponse> api = new ApiResponse<>();
        api.setResult(userService.createUser(request));
        return api;
    }

    @GetMapping()
    ApiResponse<List<UserResponse>> getAllUser() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("username:{}",authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUser())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUserById(@PathVariable String userId){

        return ApiResponse.<UserResponse>builder()
                .result(userService.findUserbyId(userId))
                .build();
    }

    @GetMapping("/myInfor")
    ApiResponse<UserResponse> getMyInfor(){

        return ApiResponse.<UserResponse>builder()
                .result(userService.getmyInfor())
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId,request))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId){
         userService.deleteUser(userId);
         return ApiResponse.<String>builder()
                 .result("User has been deleted")
                 .build();
    }
}
