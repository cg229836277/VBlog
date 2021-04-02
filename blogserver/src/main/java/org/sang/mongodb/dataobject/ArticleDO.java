package org.sang.mongodb.dataobject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "article")
@Data
public class ArticleDO extends BaseDO {

    public static final int STATUS_ALL = 2;
    public static final int STATUS_STORED = 0;
    public static final int STATUS_UNFINISHED = 1;
    public static final int STATUS_DELETED = -1;


    public String author;

    public String title;

    public String subTitle;

    public String summary;

    /**
     * 类型，不同的技术栈类型，比如Android,iOS等，用于分类展示
     */
    public String type;

    public String content;

    @Field("publish_date")
    @JsonProperty("publish_date")
    public String publishDate;

    public String tags;

    /**
     * 文章状态
     */
    public int status;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    long totalPages;

    @Transient
    @JsonSerialize
    @JsonDeserialize
    long currentPage;

}
