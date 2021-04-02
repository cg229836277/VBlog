package org.sang.mongodb.repository;

import org.sang.mongodb.dataobject.ArticleDO;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleRepository extends BaseRepository<ArticleDO> {

    private final String collectionName = "article";

    public List<ArticleDO> getByStatus(int status, int pageIndex, int pageSize) {
        if (status == ArticleDO.STATUS_ALL) {
            List<ArticleDO> items = mongoTemplate.findAll(ArticleDO.class, collectionName);
            return items;
        }
        Query articleDynamicQuery = Query.query(Criteria.where("status").is(status));//.with(pageable)
        Sort sort = Sort.by(Sort.Direction.DESC, "publish_date");
        articleDynamicQuery.with(sort);

        pageIndex = pageIndex > 0 ? pageIndex : 1;
        pageSize = pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE;
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize); // get 5 profiles on a page
        articleDynamicQuery.with(pageable);

        long allCount = mongoTemplate.count(articleDynamicQuery, ArticleDO.class);
        List<ArticleDO> items = mongoTemplate.find(articleDynamicQuery, ArticleDO.class, collectionName);
        PageImpl<ArticleDO> pageContent = (PageImpl<ArticleDO>) PageableExecutionUtils.getPage(items, pageable, () -> 0);
        return pageContent.toList();
    }
}
