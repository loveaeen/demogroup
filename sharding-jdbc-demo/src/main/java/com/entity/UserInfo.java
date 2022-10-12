package com.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author LiuCheng
 * @data 2021/2/5 15:21
 */
@Getter
@Setter
public class UserInfo {
    private Long id;
    private String userName;
    private int age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
