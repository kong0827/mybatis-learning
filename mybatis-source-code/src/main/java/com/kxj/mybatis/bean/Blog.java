
package com.kxj.mybatis.bean;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

import java.util.List;
import java.util.Map;

@Data
@ToString
@FieldNameConstants
public class Blog implements java.io.Serializable {

    private int id;
    private String title;
    private User author;
    private String body;
    private List<Comment> comments;
    private Map<String, String> labels;

    public Blog() {
    }
}
