import com.kxj.entity.Blog;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiangjin.kong
 * @date 2020/4/24 9:58
 * @desc
 */
public class MaBatisTest {

    @Test
    public void testSelect() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        Blog blog = session.selectOne("blog.selectBlog", 1);
        System.out.println(blog);
    }

    @Test
    public void testSelectAll() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        List<Blog> blog = session.selectList("blog.selectList");
        System.out.println(blog);
    }

    @Test
    public void testInsert() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        Blog blog = new Blog();
        blog.setName("lisi");
        int effectCol = session.insert("blog.insertBlog", blog);
        session.commit();
        System.out.println(blog.getId());
    }

    @Test
    public void testDelete() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        int effectCol = session.insert("blog.deleteBlog", 2);
        System.out.println(effectCol);
        session.commit();
    }

    @Test
    public void testUpdate() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        int effectCol = session.insert("blog.updateBlog", "test");
        System.out.println(effectCol);
        session.commit();
    }


    @Test
    public void batchInsert() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        List<Blog> list = new ArrayList<Blog>();
        for (int j = 0; j < 50; j++) {
            for (int i = 0; i < 10000; i++) {
                Blog blog = new Blog();
                String filename = RandomStringUtils.randomAlphanumeric(10);
                blog.setName(filename);
                list.add(blog);
            }
            int effectCol = session.insert("blog.batchInsert", list);
            session.commit();
        }

    }
}
