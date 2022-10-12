package com.mapper;

import com.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author LiuCheng
 * @data 2021/2/5 15:22
 */
public interface UserInfoMapper {
    List<UserInfo> getUserInfoList();
    int addUserInfo(UserInfo userInfo);
    int updateUserInfo(@Param("id") long id,@Param("userName") String name);
    List<UserInfo> getUserInfoListOnDepotsTable();
    int addUserInfoOnDepotsTable(UserInfo userInfo);

}
