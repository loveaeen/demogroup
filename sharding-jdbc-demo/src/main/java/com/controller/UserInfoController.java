package com.controller;

import com.entity.UserInfo;
import com.service.Im.UserInfoServiceImpl;
import com.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author LiuCheng
 * @data 2021/2/5 15:20
 */
@RequestMapping("/userInfo")
@RestController
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;
    @GetMapping("/getUserInfoList")
    List<UserInfo> getUserInfoList(){
        List<UserInfo> userInfoList=  userInfoService.getUserInfoList();
        return  userInfoList;
    }
    @GetMapping("/addUserInfo")
    Long addUserInfo(UserInfo userInfo) throws Exception{
     return    userInfoService.addUserInfo(userInfo);
    }
     @GetMapping("/updateUserInfo")
    int updateUserInfo(long id,String name){
     return userInfoService.updateUserInfo(id,name);
    }
    @GetMapping("/getUserInfoListOnDepotsTable")
    List<UserInfo> getUserInfoListOnDepotsTable(){
        List<UserInfo> userInfoList=  userInfoService.getUserInfoListOnDepotsTable();
        return  userInfoList;
    }
    @GetMapping("/addUserInfoOnDepotsTable")
    int addUserInfoOnDepotsTable(UserInfo userInfo){
        return    userInfoService.addUserInfoOnDepotsTable(userInfo);
    }
}
