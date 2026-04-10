package com.ja.itubecommon.entity.query;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 视频分类信息查询对象
 * @Author LumingJia
 * @Date 2026/04/04
 */
@Data
public class CategoryInfoQuery extends BaseQuery {
	/**
	 * 自增ID
	 */
	private Integer categoryId;

	/**
	 * 类别编码
	 */
	private String categoryCode;
	private String categoryCodeFuzzy;

	/**
	 * 类别名称
	 */
	private String categoryName;
	private String categoryNameFuzzy;

	/**
	 * 父级类别id
	 */
	private Integer pCategoryId;

	/**
	 * 类别图标
	 */
	private String icon;
	private String iconFuzzy;

	/**
	 * 背景图
	 */
	private String background;
	private String backgroundFuzzy;

	/**
	 * 排序号
	 */
	private Integer sort;

	/**
	 * 排序字段
	 */
	private String orderBy;
}