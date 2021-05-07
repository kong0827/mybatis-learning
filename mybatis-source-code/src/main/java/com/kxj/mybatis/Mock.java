package com.kxj.mybatis;

import com.kxj.mybatis.bean.Comment;
import com.kxj.mybatis.bean.User;

public class Mock {
    public static User newUser() {
        User user = new User();
        String s = System.currentTimeMillis() + "";
        s = s.substring(s.length() - 5, s.length());
        user.setName("mock_" + s);
        user.setAge("19");
        user.setEmail("modk@coderead.cn");
        user.setPhoneNumber("888888");
        user.setSex("男");
        return user;
    }

    public static Comment newComment() {
        Comment comment = new Comment();
        comment.setBody("body");
        comment.setBlogId(1);
        return comment;
    }
}
