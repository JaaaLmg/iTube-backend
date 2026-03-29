package com.ja.itubecommon.service.impl;

import com.ja.itubecommon.mappers.InfoMapper;
import com.ja.itubecommon.service.InfoService;
import com.ja.itubecommon.entity.po.Info;
import com.ja.itubecommon.entity.query.InfoQuery;
import com.ja.itubecommon.entity.query.SimplePage;
import com.ja.itubecommon.entity.enums.PageSize;
import com.ja.itubecommon.entity.vo.PaginationResultVO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 用户表业务功能实现类
 * @Author LumingJia
 * @Date 2026/03/25
 */
@Service("InfoService")
public class InfoServiceImpl implements InfoService {
	@Resource
	private InfoMapper<Info,InfoQuery> infoMapper;

	/**
	 * 根据查询条件查询列表
	 */
	@Override
	public List<Info> queryList(InfoQuery query) {
		 return this.infoMapper.selectList(query);
	}

	/**
	 * 根据查询条件查询数量
	 */
	@Override
	public Integer queryCount(InfoQuery query) {
		 return this.infoMapper.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<Info> queryPage(InfoQuery query) {
		Integer count = this.queryCount(query);
		Integer pageSize = query.getPageSize()==null?PageSize.SIZE20.getSize():query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<Info> list = this.queryList(query);
		PaginationResultVO<Info> result = new PaginationResultVO<Info>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(Info bean) {
		return this.infoMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<Info> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.infoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<Info> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.infoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据UserId查询
	 */
	@Override
	public Info getInfoByUserId(String userId) {
		return this.infoMapper.selectByUserId(userId);
	}

	/**
	 * 根据UserId更新
	 */
	@Override
	public Integer updateInfoByUserId(Info bean, String userId) {
		return this.infoMapper.updateByUserId(bean, userId);
	}

	/**
	 * 根据UserId删除
	 */
	@Override
	public Integer deleteInfoByUserId(String userId) {
		return this.infoMapper.deleteByUserId(userId);
	}

	/**
	 * 根据Email查询
	 */
	@Override
	public Info getInfoByEmail(String email) {
		return this.infoMapper.selectByEmail(email);
	}

	/**
	 * 根据Email更新
	 */
	@Override
	public Integer updateInfoByEmail(Info bean, String email) {
		return this.infoMapper.updateByEmail(bean, email);
	}

	/**
	 * 根据Email删除
	 */
	@Override
	public Integer deleteInfoByEmail(String email) {
		return this.infoMapper.deleteByEmail(email);
	}

	/**
	 * 根据Nickname查询
	 */
	@Override
	public Info getInfoByNickname(String nickname) {
		return this.infoMapper.selectByNickname(nickname);
	}

	/**
	 * 根据Nickname更新
	 */
	@Override
	public Integer updateInfoByNickname(Info bean, String nickname) {
		return this.infoMapper.updateByNickname(bean, nickname);
	}

	/**
	 * 根据Nickname删除
	 */
	@Override
	public Integer deleteInfoByNickname(String nickname) {
		return this.infoMapper.deleteByNickname(nickname);
	}

}