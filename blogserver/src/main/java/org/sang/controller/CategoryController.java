package org.sang.controller;

import lombok.extern.slf4j.Slf4j;
import org.sang.config.utils.StringUtils;
import org.sang.dataobject.CategoryDO;
import org.sang.exception.CRUDResultEnum;
import org.sang.exception.ServiceException;
import org.sang.exception.ServiceExceptionEnum;
import org.sang.service.ICategoryService;
import org.sang.vo.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/category")
@Slf4j
public class CategoryController {

    @Autowired
    ICategoryService iCategoryService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public CommonResult<List<CategoryDO>> getCategories() {
        List<CategoryDO> categoryDOList = iCategoryService.getAllCategories();
        if (categoryDOList == null || categoryDOList.size() == 0) {
            return CommonResult.error(CRUDResultEnum.GET_CATEGORY_FAIL);
        } else {
            return CommonResult.success(categoryDOList);
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult addCategory(@RequestBody final CategoryDO categoryDO) {
        if (categoryDO == null || StringUtils.isEmpty(categoryDO.getChildName()) || StringUtils.isEmpty(categoryDO.getParentName())) {
            throw new ServiceException(ServiceExceptionEnum.GET_CATEGORY_PARAMETERS_INVALID);
        }
        boolean result = iCategoryService.addCategory(categoryDO);
        return result ? CommonResult.success() : CommonResult.error(CRUDResultEnum.ADD_CATEGORY_FAIL);
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public CommonResult deleteCategory(@RequestBody final CategoryDO categoryDO) {
        boolean result = iCategoryService.deleteCategory(categoryDO.getId());
        return result ? CommonResult.success() : CommonResult.error(CRUDResultEnum.DELETE_CATEGORY_FAIL);
    }

    @RequestMapping(value = "/delete/ids", method = RequestMethod.POST)
    public CommonResult deleteCategories(@RequestBody final CategoryDO categoryDO) {
        log.info("ids is " + categoryDO.getIds().toString());
        boolean result = iCategoryService.deleteCategories(categoryDO.getIds());
        return result ? CommonResult.success() : CommonResult.error(CRUDResultEnum.DELETE_CATEGORY_FAIL);
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult updateCategory(@RequestBody final CategoryDO categoryDO) {
        if (categoryDO == null || StringUtils.isEmpty(categoryDO.getChildName()) || StringUtils.isEmpty(categoryDO.getParentName())) {
            throw new ServiceException(ServiceExceptionEnum.GET_CATEGORY_PARAMETERS_INVALID);
        }
        boolean result = iCategoryService.updateCategory(categoryDO);
        return result ? CommonResult.success() : CommonResult.error(CRUDResultEnum.UPDATE_CATEGORY_FAIL);
    }

}
