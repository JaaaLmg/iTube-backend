package com.ja.itubecommon.entity.po;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 视频分类信息
 * @Author LumingJia
 * @Date 2026/04/04
 */
@Getter
@Setter
public class CategoryInfo implements Serializable {
	/**
	 * 自增ID
	 */
	private Integer categoryId;

	/**
	 * 类别编码(唯一，一般是类别名的/英文简写）
	 */
	private String categoryCode;

	/**
	 * 类别名称
	 */
	private String categoryName;

	/**
	 * 父级类别id
	 */
	private Integer pCategoryId;

	/**
	 * 类别图标
	 */
	private String icon;

	/**
	 * 背景图
	 */
	private String background;

	/**
	 * 排序号
	 */
	private Integer sort;

	/**
	 * 子类列表
	 */
	private List<CategoryInfo> children;

	@Override
	public String toString() {
		return "视频分类信息 [" + " 自增ID:" + (categoryId == null ? "空" : categoryId) + " 类别编码:" + (categoryCode == null ? "空" : categoryCode) + " 类别名称:" + (categoryName == null ? "空" : categoryName) + " 父级类别id:" + (pCategoryId == null ? "空" : pCategoryId) + " 类别图标:" + (icon == null ? "空" : icon) + " 背景图:" + (background == null ? "空" : background) + " 排序号:" + (sort == null ? "空" : sort) + " ]";
	}
}