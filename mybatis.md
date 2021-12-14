## MyBatis

### 入门

#### SqlSessionFactoryBuilder

这个类可以被实例化、使用和丢弃，一旦创建了 SqlSessionFactory，就不再需要它了。 因此 SqlSessionFactoryBuilder 实例的最佳作用域是方法作用域（也就是局部方法变量）。 你可以重用 SqlSessionFactoryBuilder 来创建多个 SqlSessionFactory 实例，但最好还是不要一直保留着它，以保证所有的 XML 解析资源可以被释放给更重要的事情。

#### SqlSessionFactory

SqlSessionFactory 一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或重新创建另一个实例。 使用 SqlSessionFactory 的最佳实践是在应用运行期间不要重复创建多次，多次重建 SqlSessionFactory 被视为一种代码“坏习惯”。因此 SqlSessionFactory 的最佳作用域是应用作用域。 有很多方法可以做到，最简单的就是使用单例模式或者静态单例模式。

#### SqlSession

每个线程都应该有它自己的 SqlSession 实例。SqlSession 的实例不是线程安全的，因此是不能被共享的，所以它的最佳的作用域是请求或方法作用域。 绝对不能将 SqlSession 实例的引用放在一个类的静态域，甚至一个类的实例变量也不行。 也绝不能将 SqlSession 实例的引用放在任何类型的托管作用域中，比如 Servlet 框架中的 HttpSession。 如果你现在正在使用一种 Web 框架，考虑将 SqlSession 放在一个和 HTTP 请求相似的作用域中。 换句话说，每次收到 HTTP 请求，就可以打开一个 SqlSession，返回一个响应后，就关闭它。 这个关闭操作很重要，为了确保每次都能执行关闭操作，你应该把这个关闭操作放到 finally 块中。 下面的示例就是一个确保 SqlSession 关闭的标准模式：

```
try (SqlSession session = sqlSessionFactory.openSession()) {
  // 你的应用逻辑代码
}
```

在所有代码中都遵循这种使用模式，可以保证所有数据库资源都能被正确地关闭。

#### 映射器实例

映射器是一些绑定映射语句的接口。映射器接口的实例是从 SqlSession 中获得的。虽然从技术层面上来讲，任何映射器实例的最大作用域与请求它们的 SqlSession 相同。但方法作用域才是映射器实例的最合适的作用域。 也就是说，映射器实例应该在调用它们的方法中被获取，使用完毕之后即可丢弃。 映射器实例并不需要被显式地关闭。尽管在整个请求作用域保留映射器实例不会有什么问题，但是你很快会发现，在这个作用域上管理太多像 SqlSession 的资源会让你忙不过来。 因此，最好将映射器放在方法作用域内。就像下面的例子一样：

```
try (SqlSession session = sqlSessionFactory.openSession()) {
  BlogMapper mapper = session.getMapper(BlogMapper.class);
  // 你的应用逻辑代码
}
```



- 引入依赖

  ```xml
    <dependency>
             <groupId>org.mybatis</groupId>
             <artifactId>mybatis</artifactId>
             <version>3.4.6</version>
         </dependency>
  
         <dependency>
             <groupId>mysql</groupId>
             <artifactId>mysql-connector-java</artifactId>
             <version>5.1.46</version>
         </dependency>
  ```

-   建立实体类

  ```xml
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
  
  ```

- XML定义SQL语句

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE mapper
          PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
  <mapper namespace="blog">
      <select id="selectBlog" resultType="com.kxj.entity.Blog">
          select * from Blog where id = #{id}
       </select>
  </mapper>
  ```

  

- MyBatis 核心设置 配置文件

  1、 必须指定命名空间 

  ```xml
  <?xml version="1.0" encoding="UTF-8" ?>
  <!DOCTYPE configuration
          PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
          "http://mybatis.org/dtd/mybatis-3-com.config.dtd">
  <configuration>
      <!-- 和spring整合后 environments配置将废除 -->
      <environments default="development">
          <environment id="development">
              <!-- 使用jdbc事务管理 -->
              <transactionManager type="JDBC" />
              <!-- 数据库连接池 -->
              <dataSource type="POOLED">
                  <property name="driver" value="com.mysql.jdbc.Driver" />
                  <property name="url"
                            value="jdbc:mysql://localhost:3306/mybatis?characterEncoding=utf-8" />
                  <property name="username" value="root" />
                  <property name="password" value="root" />
              </dataSource>
          </environment>
      </environments>
  
      <!-- 加载映射文件 -->
      <mappers>
          <mapper resource="blog.xml"/>
      </mappers>
  </configuration>
  ```

- 测试

  1、 每个基于 MyBatis 的应用都是以一个 `SqlSessionFactory` 的实例为核心的。`SqlSessionFactory` 的实例可以通过 `SqlSessionFactoryBuilder` 获得。而 `SqlSessionFactoryBuilder` 则可以从 XML 配置文件或一个预先配置的 `Configuration` 实例来构建出` SqlSessionFactory `实例。 

  2、 方式和用全限定名调用 Java 对象的方法类似。这样，该命名就可以直接映射到在命名空间中同名的映射器类，并将已映射的 select 语句匹配到对应名称、参数和返回类型的方法。 

  ```java
  public class MaBatisTest {
  
      @Test
      public void test() throws IOException {
          InputStream inputStream = Resources.getResourceAsStream("sqlMapConfig.xml");
          SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
          SqlSession session = sqlSessionFactory.openSession();
          Blog blog = session.selectOne("blog.selectBlog", 1);
          System.out.println(blog);
      }
  }
  ```

  

  