package com.ja.itubecommon.service.impl;

import com.ja.itubecommon.component.RedisComponent;
import com.ja.itubecommon.exception.BusinessException;
import com.ja.itubecommon.mappers.CategoryInfoMapper;
import com.ja.itubecommon.service.CategoryInfoService;
import com.ja.itubecommon.entity.po.CategoryInfo;
import com.ja.itubecommon.entity.query.CategoryInfoQuery;
import com.ja.itubecommon.entity.query.SimplePage;
import com.ja.itubecommon.entity.enums.PageSize;
import com.ja.itubecommon.entity.vo.PaginationResultVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 视频分类信息业务功能实现类
 * @Author LumingJia
 * @Date 2026/04/04
 */
@Service("CategoryInfoService")
public class CategoryInfoServiceImpl implements CategoryInfoService {
	@Resource
	private CategoryInfoMapper<CategoryInfo,CategoryInfoQuery> categoryInfoMapper;

	@Resource
	private RedisComponent redisComponent;

	/**
	 * 根据查询条件查询列表
	 */
	@Override
	public List<CategoryInfo> queryList(CategoryInfoQuery query) {
		 return this.categoryInfoMapper.selectList(query);
	}

	/**
	 * 根据查询条件查询树形列表
	 */
	@Override
	public List<CategoryInfo> queryListTree(CategoryInfoQuery query, Integer rootId) {
		List<CategoryInfo> list = this.queryList(query);
		return convertList2Tree(list, rootId);
	}

	private List<CategoryInfo> convertList2Tree(List<CategoryInfo> list, Integer rootId) {
		List<CategoryInfo> childrenList = new ArrayList<>();
		for(CategoryInfo ci : list) {
			if (ci.getPCategoryId()==null || ci.getCategoryId()==null) continue;
			if (ci.getPCategoryId().equals(rootId)) {
				ci.setChildren(convertList2Tree(list, ci.getCategoryId()));
				childrenList.add(ci);
			}
		}
		return childrenList;
	}

	/**
	 * 根据查询条件查询数量
	 */
	@Override
	public Integer queryCount(CategoryInfoQuery query) {
		 return this.categoryInfoMapper.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<CategoryInfo> queryPage(CategoryInfoQuery query) {
		Integer count = this.queryCount(query);
		Integer pageSize = query.getPageSize()==null?PageSize.SIZE20.getSize():query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<CategoryInfo> list = this.queryList(query);
		PaginationResultVO<CategoryInfo> result = new PaginationResultVO<CategoryInfo>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(CategoryInfo bean) {
		return this.categoryInfoMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<CategoryInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.categoryInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<CategoryInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.categoryInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据CategoryId查询
	 */
	@Override
	public CategoryInfo getCategoryInfoByCategoryId(Integer categoryId) {
		return this.categoryInfoMapper.selectByCategoryId(categoryId);
	}

	/**
	 * 根据CategoryId更新
	 */
	@Override
	public Integer updateCategoryInfoByCategoryId(CategoryInfo bean, Integer categoryId) {
		return this.categoryInfoMapper.updateByCategoryId(bean, categoryId);
	}

	/**
	 * 根据CategoryId删除
	 */
	@Override
	public Integer deleteCategoryInfoByCategoryId(Integer categoryId) {
		return this.categoryInfoMapper.deleteByCategoryId(categoryId);
	}

	/**
	 * 根据CategoryId递归删除
	 */
	@Override
	public Integer RecursivelyDeleteCategoryInfoByCategoryId(Integer categoryId) {
		CategoryInfoQuery query = new CategoryInfoQuery();
		query.setPCategoryId(categoryId);
		List<CategoryInfo> children = this.queryList(query);

		for (CategoryInfo child : children) {
			RecursivelyDeleteCategoryInfoByCategoryId(child.getCategoryId());
		}
		// 刷新缓存
		this.saveToRedis();
		return this.deleteCategoryInfoByCategoryId(categoryId);
	}

	/**
	 * 根据CategoryCode查询
	 */
	@Override
	public CategoryInfo getCategoryInfoByCategoryCode(String categoryCode) {
		return this.categoryInfoMapper.selectByCategoryCode(categoryCode);
	}

	/**
	 * 根据CategoryCode更新
	 */
	@Override
	public Integer updateCategoryInfoByCategoryCode(CategoryInfo bean, String categoryCode) {
		return this.categoryInfoMapper.updateByCategoryCode(bean, categoryCode);
	}

	/**
	 * 根据CategoryCode删除
	 */
	@Override
	public Integer deleteCategoryInfoByCategoryCode(String categoryCode) {
		return this.categoryInfoMapper.deleteByCategoryCode(categoryCode);
	}

	@Override
	public void saveCategoryInfo(CategoryInfo bean) throws BusinessException {
		CategoryInfo selectedBean = this.getCategoryInfoByCategoryCode(bean.getCategoryCode());
		if (bean.getCategoryId()==null) {	// 新增
			if (selectedBean!=null) {
				throw new BusinessException("类别编码已存在");
			} else {
				bean.setSort(this.categoryInfoMapper.selectMaxSort(bean.getPCategoryId()) + 1);
				this.categoryInfoMapper.insert(bean);
			}
		} else {	// 修改
			if (selectedBean!=null && !selectedBean.getCategoryId().equals(bean.getCategoryId())) {
				throw new BusinessException("类别编码已存在");
			} else {
				this.categoryInfoMapper.updateByCategoryId(bean, bean.getCategoryId());
			}
		}
		// 刷新缓存
		this.saveToRedis();
	}

	@Override
	public void changeSort(Integer pCategoryId, String categoryIds) throws BusinessException {
		String[] idsArr = categoryIds.split(",");
		List<CategoryInfo> categoryInfoList = new ArrayList<>();
		Integer sort = 1;
		for (String id : idsArr) {
			CategoryInfo categoryInfo = new CategoryInfo();
			categoryInfo.setPCategoryId(pCategoryId);
			categoryInfo.setCategoryId(Integer.parseInt(id));
			categoryInfo.setSort(sort++);
			categoryInfoList.add(categoryInfo);
		}
		this.categoryInfoMapper.updateSort(categoryInfoList);
		// 刷新缓存
		this.saveToRedis();
	}

	/**
	 * 更新缓存
	 */
	private void saveToRedis() {
		CategoryInfoQuery query = new CategoryInfoQuery();
		query.setOrderBy("sort asc");
		List<CategoryInfo> categoryInfoList = this.queryListTree(query, 0);
		redisComponent.saveCategoryInfoList(categoryInfoList);
	}

}