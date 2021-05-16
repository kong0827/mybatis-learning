package com.kxj.mybatis;

import com.kxj.mybatis.bean.Blog;
import com.kxj.mybatis.bean.Comment;
import com.kxj.mybatis.bean.User;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.BeanWrapper;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

import java.util.*;

/**
 * @author kxj
 * @date 2021/5/7 23:51
 * @desc
 */

public class MetaObjectTest {

    @Test
    public void test() {
        // 装饰器模式
        Blog blog = new Blog();
        Configuration configuration = new Configuration();
        MetaObject metaObject = configuration.newMetaObject(blog);
        metaObject.setValue("title", "测试标题");
        System.out.println(metaObject.getValue("title"));

        // 设置对象
        metaObject.setValue(Blog.Fields.author.concat(".").concat(User.Fields.age), "20");
        System.out.println(metaObject.getValue(Blog.Fields.author.concat(".").concat(User.Fields.age)));

        // 设置数组 需要手动设置
        metaObject.setValue("comments", new ArrayList<>());
        metaObject.setValue("comments", Collections.singletonList(Mock.newComment()));
        System.out.println(metaObject.getValue("comments[0].body"));

        // 设置Map 需要手动设置
        metaObject.setValue("labels", new HashMap<>());
        metaObject.setValue("labels[key]", "value");
        System.out.println(metaObject.getValue("labels[key]"));
        Map<String, String> labels = blog.getLabels();


        // BeanWrapper
        BeanWrapper beanWrapper = new BeanWrapper(metaObject, blog);
        Object title = beanWrapper.get(new PropertyTokenizer("title"));
        System.out.println(title);

    }
}
