package com.kxj.entity;

import java.io.Serializable;

/**
 * @author xiangjin.kong
 * @date 2020/4/24 9:57
 * @desc
 */
public class Blog implements Serializable {
    private Integer id;
    private String name;

    public Blog() {
    }

    public Blog(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
