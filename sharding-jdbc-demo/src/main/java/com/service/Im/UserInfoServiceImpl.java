package com.service.Im;

import com.entity.UserInfo;
import com.mapper.UserInfoMapper;
import com.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author LiuCheng
 * @data 2021/2/5 15:21
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 获取所有用户
     *
     * @return
     */
    @Override
    public List<UserInfo> getUserInfoList() {

        List<UserInfo> userInfoList = userInfoMapper.getUserInfoList();
        return userInfoList;
    }

    /**
     * 插入数据
     *
     * @param userInfo
     * @return
     */
    @Override
    public Long addUserInfo(UserInfo userInfo) throws Exception {
        userInfoMapper.addUserInfo(userInfo);
        return  userInfo.getId();
    }

    @Override
    public int getI() throws Exception {
        int i = 0;
        try {
            i = 1 / 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return i;
    }

    /**
     * 修改
     *
     *
     * @param id
     * @param name
     * @return
     */
    @Override
    public int updateUserInfo(long id, String name) {
        return userInfoMapper.updateUserInfo(id, name);
    }

    @Override
    public List<UserInfo> getUserInfoListOnDepotsTable() {
        return userInfoMapper.getUserInfoListOnDepotsTable();
    }

    @Override
    public int addUserInfoOnDepotsTable(UserInfo userInfo) {
        return userInfoMapper.addUserInfoOnDepotsTable(userInfo);
    }


}
