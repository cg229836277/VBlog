package org.sang.mongodb.autoinckey;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//@Document  把一个java类声明为mongodb的文档，可以通过collection参数指定这个类对应的文档
@Document(collection = "sequence")
@Data
public class SeqInfo {
    @Id
    private String id;//主键
    private Long seqId;//序列值
    private String collName;//集合名称
}
