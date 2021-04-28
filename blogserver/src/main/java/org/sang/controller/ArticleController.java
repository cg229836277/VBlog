package org.sang.controller;

import lombok.extern.slf4j.Slf4j;
import org.sang.config.utils.StringUtils;
import org.sang.dataobject.CategoryDO;
import org.sang.exception.CRUDResultEnum;
import org.sang.ratelimit.Limit;
import org.sang.ratelimit.LimitParas;
import org.sang.ratelimit.LimitType;
import org.sang.service.ICategoryService;
import org.sang.vo.ArticleDataObject;
import org.sang.exception.ServiceException;
import org.sang.exception.ServiceExceptionEnum;
import org.sang.mongodb.dataobject.ArticleDO;
import org.sang.service.IArticleService;
import org.sang.vo.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sang on 2017/12/20.
 */
@RestController
@RequestMapping("/article")
@Slf4j
public class ArticleController {

    @Autowired
    IArticleService iArticleService;

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/delete/ids", method = RequestMethod.POST)
    public CommonResult deleteArticle(@RequestBody ArticleDO articleDO) {
        if (articleDO == null) {
            log.info("articleDO is null");
            return CommonResult.error(CRUDResultEnum.DELETE_ARTICLE_FAIL);
        }
        log.info("id is " + Arrays.toString(articleDO.getIds()));
        boolean result = iArticleService.deleteArticle(articleDO.getIds());
        if (!result) {
            return CommonResult.error(CRUDResultEnum.DELETE_ARTICLE_FAIL);
        } else {
            return CommonResult.success();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public CommonResult addNewArticle(@RequestBody ArticleDO article) {
        if (article == null) {
            log.info("data is null");
            throw new ServiceException(ServiceExceptionEnum.SAVE_ARTICLE_FAIL);
        }
        log.info("data is " + article.toString());
        ArticleDO result = iArticleService.insert(article);
        if (result == null) {
            return CommonResult.error(CRUDResultEnum.GET_ARTICLE_FAIL);
        } else {
            return CommonResult.success();
        }
    }

    @RequestMapping(value = "/id/{id}", method = RequestMethod.GET)
    @Limit(key = "article_id", period = LimitParas.ARTICLE_TIME_LIMIT, count = LimitParas.ARTICLE_COUNT_LIMIT, limitType = LimitType.IP)
    public CommonResult<ArticleDO> getArticleById(@PathVariable String id) {
        ArticleDO data = iArticleService.getById(id);
        if (data == null) {
            return CommonResult.error(CRUDResultEnum.GET_ARTICLE_FAIL);
        } else {
            return CommonResult.success(data);
        }
    }

    @RequestMapping(value = "/{type}", method = RequestMethod.GET)
    @Limit(key = "article_type", period = LimitParas.ARTICLE_TIME_LIMIT, count = LimitParas.ARTICLE_COUNT_LIMIT, limitType = LimitType.IP)
    public CommonResult<List<ArticleDO>> getArticleByType(@PathVariable String type) {
        List<ArticleDO> data = iArticleService.getByType(type);
        if (data == null) {
            return CommonResult.error(CRUDResultEnum.GET_ARTICLE_FAIL);
        } else {
            return CommonResult.success(data);
        }
    }

    @Limit(key = "article_title", period = LimitParas.ARTICLE_TIME_LIMIT, count = LimitParas.ARTICLE_COUNT_LIMIT, limitType = LimitType.IP)
    @RequestMapping(value = "/title/{title}", method = RequestMethod.GET)
    public CommonResult<ArticleDO> getArticleByTitle(@PathVariable String title) {
        ArticleDO data = iArticleService.getByTitle(title);
        if (data == null) {
            return CommonResult.error(CRUDResultEnum.GET_ARTICLE_FAIL);
        } else {
            return CommonResult.success(data);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult updateArticle(@RequestBody ArticleDO article) {
        boolean result = iArticleService.update(article);
        if (!result) {
            return CommonResult.error(CRUDResultEnum.UPDATE_ARTICLE_FAIL);
        } else {
            return CommonResult.success();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/update_status", method = RequestMethod.POST)
    public CommonResult updateArticleByIds(@RequestBody ArticleDO article) {
        if (article == null) {
            log.info("articleDO is null");
            return CommonResult.error(CRUDResultEnum.UPDATE_ARTICLE_FAIL);
        }
        log.info("id is " + Arrays.toString(article.getIds()));
        boolean result = iArticleService.updateStatusByIds(article.getIds(), article.getStatus());
        if (!result) {
            return CommonResult.error(CRUDResultEnum.UPDATE_ARTICLE_FAIL);
        } else {
            return CommonResult.success();
        }
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @Limit(key = "article_status", period = LimitParas.ARTICLE_TIME_LIMIT, count = LimitParas.ARTICLE_COUNT_LIMIT, limitType = LimitType.IP)
    public CommonResult<ArticleDataObject> getArticleByStatus(String status, String pageIndex, String pageSize) {
        log.info("status:" + status + ",pageIndex:" + pageIndex + ",pageSize:" + pageSize);
        int defaultStatus = ArticleDO.STATUS_ALL;
        int defaultPageIndex = 1, defaultPageSize = 8;
        try {
            defaultStatus = Integer.parseInt(status);
            defaultPageIndex = Integer.parseInt(pageIndex);
            defaultPageSize = Integer.parseInt(pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("defaultStatus:" + defaultStatus + ",defaultPageIndex:" + defaultPageIndex + ",defaultPageSize:" + defaultPageSize);
        ArticleDataObject data = iArticleService.getByStatus(defaultStatus, defaultPageIndex, defaultPageSize);
        return CommonResult.success(data);
    }

    @RequestMapping(value = "/categoryId/{categoryId}", method = RequestMethod.GET)
    @Limit(key = "article_categoryId", period = LimitParas.ARTICLE_TIME_LIMIT, count = LimitParas.ARTICLE_COUNT_LIMIT, limitType = LimitType.IP)
    public CommonResult<List<ArticleDO>> getArticleByCategoryId(@PathVariable int categoryId) {
        List<ArticleDO> data = iArticleService.getByCategoryId(categoryId);
        if (data == null) {
            return CommonResult.error(CRUDResultEnum.GET_ARTICLE_FAIL);
        } else {
            return CommonResult.success(data);
        }
    }
}
