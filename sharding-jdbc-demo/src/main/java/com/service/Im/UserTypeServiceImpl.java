package com.service.Im;

import com.entity.UserType;
import com.mapper.UserTypeMapper;
import com.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserTypeServiceImpl implements UserTypeService {

    @Autowired
    private UserTypeMapper userTypeMapper;

    @Override
    public List<UserType> getUserTypeList() {
        return userTypeMapper.getUserTypeList();
    }
}
