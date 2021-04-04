package org.sang.dataobject;

import lombok.Data;
import org.sang.mongodb.dataobject.ArticleDO;

import java.util.List;

@Data
public class ArticleDataObject {
    public List<ArticleDO> articleDOList;

    public long pageSize;

    public long pageIndex;
}
