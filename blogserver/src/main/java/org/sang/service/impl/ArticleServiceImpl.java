package org.sang.service.impl;

import org.sang.vo.ArticleDataObject;
import org.sang.mongodb.dataobject.ArticleDO;
import org.sang.mongodb.repository.ArticleRepository;
import org.sang.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements IArticleService {

    private final String collectionName = "article";

    @Autowired
    ArticleRepository articleRepository;

    @Override
    public ArticleDO insert(ArticleDO data) {
        return articleRepository.insert(data);
    }

    @Override
    public ArticleDO update(ArticleDO data) {
        return articleRepository.update(data, collectionName);
    }

    @Override
    public List<ArticleDO> getByType(String type) {
        return articleRepository.getByType(type, collectionName);
    }

    @Override
    public ArticleDataObject getByStatus(int status, int pageIndex, int pageSize) {
        return articleRepository.getByStatus(status, pageIndex, pageSize);
    }

    @Override
    public ArticleDO getById(String id) {
        return articleRepository.getById(id);
    }
}
