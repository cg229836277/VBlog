package org.sang.vo;

import lombok.Data;
import org.sang.mongodb.dataobject.ArticleDO;

import java.util.List;

@Data
public class ArticleDataInListObject {
    List<ArticleDO> dataList;
    String parentName;
    String childName;
}
