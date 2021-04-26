package org.sang.mongodb.dataobject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.sang.mongodb.autoinckey.AutoIncKey;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "article")
@Data
public class ArticleDO {
    public static final int STATUS_ABOUT_ARTICLE = 3;
    public static final int STATUS_ALL = 2;
    public static final int STATUS_STORED = 0;
    public static final int STATUS_UNFINISHED = 1;
    public static final int STATUS_DELETED = -1;

    public static final String TYPE_ARTICLE = "article";

    @AutoIncKey
    @Id
    protected long id;

    @Field("create_time")
    @JsonProperty("create_time")
    protected String createTime;

    public String author;

    public String title;

    public String subTitle;

    public String summary;

    /**
     * 类型，不同的技术栈类型，比如Android,iOS等，用于分类展示
     */
    public String type;

    @Field("origin_content")
    public String originContent;

    @Field("html_content")
    public String htmlContent;

    @Field("publish_date")
    @JsonProperty("publish_date")
    public String publishDate;

    public String[] tags;

    /**
     * 文章状态
     */
    public int status;

    @Field("category_id")
    @JsonProperty("category_id")
    public int categoryId;

    @Transient
    public String[] ids;

}
