package org.sang.service;

import org.sang.mongodb.dataobject.ArticleDO;

import java.util.List;

public interface IArticleService {
    public ArticleDO insert(ArticleDO data);

    public ArticleDO update(ArticleDO data);

    public List<ArticleDO> getByType(String type);
}
