package org.sang.controller;

import lombok.extern.slf4j.Slf4j;
import org.sang.dataobject.ArticleDataObject;
import org.sang.exception.ServiceException;
import org.sang.exception.ServiceExceptionEnum;
import org.sang.mongodb.dataobject.ArticleDO;
import org.sang.service.IArticleService;
import org.sang.vo.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public CommonResult addNewArticle(@RequestBody ArticleDO article) {
        if (article == null) {
            log.info("data is null");
            throw new ServiceException(ServiceExceptionEnum.SAVE_ARTICLE_FAIL);
        }
        log.info("data is " + article.toString());
        ArticleDO result = iArticleService.insert(article);
        if (result == null) {
            throw new ServiceException(ServiceExceptionEnum.SAVE_ARTICLE_FAIL);
        } else {
            return CommonResult.success();
        }
    }

    @RequestMapping(value = "/{type}", method = RequestMethod.GET)
    public CommonResult<List<ArticleDO>> getArticleByType(@PathVariable String type) {
        List<ArticleDO> data = iArticleService.getByType(type);
        if (data == null) {
            throw new ServiceException(ServiceExceptionEnum.GET_ARTICLE_FAIL);
        } else {
            return CommonResult.success(data);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult updateArticle(@RequestBody ArticleDO article) {
        ArticleDO data = iArticleService.update(article);
        if (data == null) {
            throw new ServiceException(ServiceExceptionEnum.UPDATE_ARTICLE_FAIL);
        } else {
            return CommonResult.success();
        }
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public CommonResult<ArticleDataObject> getArticleByStatus(int status, int pageIndex, int pageSize) {
        ArticleDataObject data = iArticleService.getByStatus(status, pageIndex, pageSize);
        if (data == null) {
            throw new ServiceException(ServiceExceptionEnum.UPDATE_ARTICLE_FAIL);
        } else {
            return CommonResult.success(data);
        }
    }
}
