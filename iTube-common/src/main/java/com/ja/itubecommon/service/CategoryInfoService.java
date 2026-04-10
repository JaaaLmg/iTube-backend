package com.ja.itubecommon.service;

import com.ja.itubecommon.entity.po.CategoryInfo;
import com.ja.itubecommon.entity.query.CategoryInfoQuery;
import com.ja.itubecommon.entity.vo.PaginationResultVO;
import com.ja.itubecommon.exception.BusinessException;

import java.util.List;

/**
 * @Description 视频分类信息Service接口
 * @Author LumingJia
 * @Date 2026/04/04
 */
public interface CategoryInfoService {
	/**
	 * 根据查询条件查询列表
	 */
	List<CategoryInfo> queryList(CategoryInfoQuery query);

	/**
	 * 根据查询条件查询树形列表
	 */
	List<CategoryInfo> queryListTree(CategoryInfoQuery query, Integer rootId);

	/**
	 * 根据查询条件查询数量
	 */
	Integer queryCount(CategoryInfoQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<CategoryInfo> queryPage(CategoryInfoQuery query);

	/**
	 * 新增
	 */
	Integer add(CategoryInfo bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<CategoryInfo> listBean);

	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<CategoryInfo> listBean);

	/**
	 * 根据CategoryId查询
	 */
	CategoryInfo getCategoryInfoByCategoryId(Integer categoryId);


	/**
	 * 根据CategoryId更新
	 */
	Integer updateCategoryInfoByCategoryId(CategoryInfo bean, Integer categoryId);


	/**
	 * 根据CategoryId删除
	 */
	Integer deleteCategoryInfoByCategoryId(Integer categoryId);

	/**
	 * 根据CategoryId递归删除
	 */
	Integer RecursivelyDeleteCategoryInfoByCategoryId(Integer categoryId);

	/**
	 * 根据CategoryCode查询
	 */
	CategoryInfo getCategoryInfoByCategoryCode(String categoryCode);


	/**
	 * 根据CategoryCode更新
	 */
	Integer updateCategoryInfoByCategoryCode(CategoryInfo bean, String categoryCode);


	/**
	 * 根据CategoryCode删除
	 */
	Integer deleteCategoryInfoByCategoryCode(String categoryCode);

	/**
	 *	新增或修改
	 */
	void saveCategoryInfo(CategoryInfo categoryInfo) throws BusinessException;

	/**
	 * 修改排序
	 */
	void changeSort(Integer pCategoryId, String categoryIds) throws BusinessException;

}