package com.example.demo.pojo;

import java.io.Serializable;

public class SysUser implements Serializable {

    private String name;

    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public SysUser(String name, String age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "SysUser{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
