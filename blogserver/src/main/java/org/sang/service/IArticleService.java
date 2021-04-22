package org.sang.service;

import org.sang.vo.ArticleDataObject;
import org.sang.mongodb.dataobject.ArticleDO;

import java.util.List;

public interface IArticleService {
    ArticleDO insert(ArticleDO data);

    ArticleDO update(ArticleDO data);

    List<ArticleDO> getByType(String type);

    ArticleDO getById(String id);

    ArticleDataObject getByStatus(int status, int pageIndx, int pageSize);

    List<ArticleDO> getByCategoryId(int categoryId);

    boolean deleteArticle(String[] ids);

    boolean updateStatusByIds(String[] ids, int status);
}
