package com.service;

import com.entity.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LiuCheng
 * @data 2021/2/5 15:40
 */
public interface UserInfoService {
    List<UserInfo> getUserInfoList();
    Long addUserInfo(UserInfo userInfo) throws Exception;
    int updateUserInfo(long id,String name);
    List<UserInfo> getUserInfoListOnDepotsTable();
    int addUserInfoOnDepotsTable(UserInfo userInfo);
    int getI() throws Exception;

}
