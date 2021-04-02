package org.sang.mongodb.dataobject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.sang.mongodb.autoinckey.AutoIncKey;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
public class BaseDO {

    public static final String TYPE_ARTICLE = "article";

    @AutoIncKey
    @Id
    protected long id;

    @Field("create_time")
    @JsonProperty("create_time")
    protected Date createTime;
}
