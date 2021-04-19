package org.sang.mongodb.repository;

import org.sang.vo.ArticleDataObject;
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

    public ArticleDO getById(String id) {
        Query articleQuery = Query.query(Criteria.where("_id").is(Long.parseLong(id)));
        ArticleDO items = mongoTemplate.findOne(articleQuery, ArticleDO.class, collectionName);
        return items;
    }

    public ArticleDataObject getByStatus(int status, int pageIndex, int pageSize) {
        if (status == ArticleDO.STATUS_ALL) {
            List<ArticleDO> items = mongoTemplate.findAll(ArticleDO.class, collectionName);
            if (items == null || items.size() == 0) {
                return null;
            }
            ArticleDataObject articleDataObject = new ArticleDataObject();
            articleDataObject.setArticleDOList(items);
            articleDataObject.setPageIndex(pageIndex);
            articleDataObject.setPageSize(items.size());
            return articleDataObject;
        }
        Query articleDynamicQuery = Query.query(Criteria.where("status").is(status));//.with(pageable)
        Sort sort = Sort.by(Sort.Direction.DESC, "publish_date");
        articleDynamicQuery.fields().include("author").include("title").include("type").include("publish_date");
        articleDynamicQuery.with(sort);
        long totalCount = mongoTemplate.count(articleDynamicQuery, ArticleDO.class);
        long totalPageSize = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;

        pageIndex = pageIndex > 0 ? pageIndex : 1;
        pageSize = pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE;
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize); // get 5 profiles on a page
        articleDynamicQuery.with(pageable);

        List<ArticleDO> items = mongoTemplate.find(articleDynamicQuery, ArticleDO.class, collectionName);
        PageImpl<ArticleDO> pageContent = (PageImpl<ArticleDO>) PageableExecutionUtils.getPage(items, pageable, () -> 0);
        List<ArticleDO> dataList = pageContent.toList();
        if (dataList == null || dataList.size() == 0) {
            return null;
        }

        ArticleDataObject articleDataObject = new ArticleDataObject();
        articleDataObject.setArticleDOList(dataList);
        articleDataObject.setPageIndex(pageIndex);
        articleDataObject.setPageSize(totalPageSize);
        return articleDataObject;
    }

    public List<ArticleDO> getByCategoryId(int categoryId) {
        Query articleDynamicQuery = Query.query(Criteria.where("category_id").is(categoryId));
        Sort sort = Sort.by(Sort.Direction.DESC, "publish_date");
        articleDynamicQuery.with(sort);
        List<ArticleDO> items = mongoTemplate.find(articleDynamicQuery, ArticleDO.class, collectionName);
        return items;
    }
}
