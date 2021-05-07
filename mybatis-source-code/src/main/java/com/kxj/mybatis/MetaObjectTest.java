package com.kxj.mybatis;

import com.kxj.mybatis.bean.Blog;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

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
    }
}
