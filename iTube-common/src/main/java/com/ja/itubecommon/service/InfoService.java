package com.ja.itubecommon.service;

import com.ja.itubecommon.entity.po.Info;
import com.ja.itubecommon.entity.query.InfoQuery;
import com.ja.itubecommon.entity.vo.PaginationResultVO;
import java.util.List;

/**
 * @Description 用户表Service接口
 * @Author LumingJia
 * @Date 2026/03/25
 */
public interface InfoService {
	/**
	 * 根据查询条件查询列表
	 */
	List<Info> queryList(InfoQuery query);

	/**
	 * 根据查询条件查询数量
	 */
	Integer queryCount(InfoQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<Info> queryPage(InfoQuery query);

	/**
	 * 新增
	 */
	Integer add(Info bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<Info> listBean);

	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<Info> listBean);

	/**
	 * 根据UserId查询
	 */
	Info getInfoByUserId(String userId);


	/**
	 * 根据UserId更新
	 */
	Integer updateInfoByUserId(Info bean, String userId);


	/**
	 * 根据UserId删除
	 */
	Integer deleteInfoByUserId(String userId);


	/**
	 * 根据Email查询
	 */
	Info getInfoByEmail(String email);


	/**
	 * 根据Email更新
	 */
	Integer updateInfoByEmail(Info bean, String email);


	/**
	 * 根据Email删除
	 */
	Integer deleteInfoByEmail(String email);


	/**
	 * 根据Nickname查询
	 */
	Info getInfoByNickname(String nickname);


	/**
	 * 根据Nickname更新
	 */
	Integer updateInfoByNickname(Info bean, String nickname);


	/**
	 * 根据Nickname删除
	 */
	Integer deleteInfoByNickname(String nickname);


}