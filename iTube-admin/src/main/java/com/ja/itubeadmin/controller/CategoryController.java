package com.ja.itubeadmin.controller;

import com.ja.itubecommon.entity.po.CategoryInfo;
import com.ja.itubecommon.entity.query.CategoryInfoQuery;
import com.ja.itubecommon.entity.vo.ResponseVO;
import com.ja.itubecommon.exception.BusinessException;
import com.ja.itubecommon.service.CategoryInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Validated
@RequestMapping("category")
public class CategoryController extends BaseController{
    @Resource
    private CategoryInfoService categoryInfoService;

    /**
     * 查询视频分类列表
     */
    @RequestMapping("loadCategory")
    public ResponseVO loadCategory(CategoryInfoQuery query) throws BusinessException {
        query.setOrderBy("sort asc");
        List<CategoryInfo> categoryInfoList = categoryInfoService.queryListTree(query, 0);
        return getSuccessResponseVO(categoryInfoList);
    }

    /**
     *  新增或修改视频分类信息
     */
    @RequestMapping("saveCategory")
    public ResponseVO saveCategory(Integer categoryId,  // 如果categoryId不为空，则修改，否则新增
                                   @NotNull Integer pCategoryId,
                                   @NotEmpty String categoryCode,
                                   @NotEmpty String categoryName,
                                   String icon,
                                   String background) throws BusinessException{
        CategoryInfo categoryInfo = new CategoryInfo();
        categoryInfo.setCategoryId(categoryId);
        categoryInfo.setPCategoryId(pCategoryId);
        categoryInfo.setCategoryCode(categoryCode);
        categoryInfo.setCategoryName(categoryName);
        categoryInfo.setIcon(icon);
        categoryInfo.setBackground(background);

        categoryInfoService.saveCategoryInfo(categoryInfo);
        return getSuccessResponseVO(null);
    }

    /**
     *  删除视频分类信息（递归删除）
     */
    @RequestMapping("deleteCategory")
    public ResponseVO deleteCategory(@NotNull Integer categoryId) throws BusinessException {
        categoryInfoService.RecursivelyDeleteCategoryInfoByCategoryId(categoryId);
        return getSuccessResponseVO(null);
    }

    /**
     *  修改排序
     *  categoryIds: 逗号分隔的categoryId列表
     *  pCategoryId: 父级类别
     */
    @RequestMapping("changeSort")
    public ResponseVO changeSort(@NotNull Integer pCategoryId,
                                 @NotEmpty String categoryIds) throws BusinessException {
        categoryInfoService.changeSort(pCategoryId, categoryIds);
        return getSuccessResponseVO(null);
    }

}
