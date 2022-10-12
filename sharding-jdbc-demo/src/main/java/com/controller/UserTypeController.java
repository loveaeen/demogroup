package com.controller;

import com.entity.UserType;
import com.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LiuCheng
 * @data 2021/2/5 15:20
 */
@RequestMapping("/userType")
@RestController
public class UserTypeController {
    @Autowired
    private UserTypeService userTypeService;
    @GetMapping("/getUserTypeList")
    List<UserType> getUserTypeList(){
        return userTypeService.getUserTypeList();
    }

}
