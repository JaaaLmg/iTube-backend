package com.ja.itubecommon.mappers;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 视频分类信息Mapper
 * @Author LumingJia
 * @Date 2026/04/04
 */
public interface CategoryInfoMapper<T, P> extends BaseMapper {
	/**
	 * 根据CategoryId查询
	 */
	T selectByCategoryId(@Param("categoryId") Integer categoryId);

	/**
	 * 根据CategoryId更新
	 */
	Integer updateByCategoryId(@Param("bean") T t, @Param("categoryId") Integer categoryId);

	/**
	 * 根据CategoryId删除
	 */
	Integer deleteByCategoryId(@Param("categoryId") Integer categoryId);

	/**
	 * 根据CategoryCode查询
	 */
	T selectByCategoryCode(@Param("categoryCode") String categoryCode);

	/**
	 * 根据CategoryCode更新
	 */
	Integer updateByCategoryCode(@Param("bean") T t, @Param("categoryCode") String categoryCode);

	/**
	 * 根据CategoryCode删除
	 */
	Integer deleteByCategoryCode(@Param("categoryCode") String categoryCode);

	/**
	 * 查找同一父级类别的分类的排序号最大值
	 */
	Integer selectMaxSort(@Param("pCategoryId") Integer pCategoryId);

	/**
	 * 更新排序号
	 */
	Integer updateSort(@Param("categoryInfoList") List<T> categoryInfoList);
}