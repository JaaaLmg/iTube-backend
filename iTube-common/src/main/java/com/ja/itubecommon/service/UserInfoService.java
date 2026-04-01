package com.ja.itubecommon.service;

import com.ja.itubecommon.entity.dto.TokenUserInfoDto;
import com.ja.itubecommon.entity.po.UserInfo;
import com.ja.itubecommon.entity.query.UserInfoQuery;
import com.ja.itubecommon.entity.vo.PaginationResultVO;
import com.ja.itubecommon.exception.BusinessException;

import java.util.List;

/**
 * @Description 用户表Service接口
 * @Author LumingJia
 * @Date 2026/03/25
 */
public interface UserInfoService {
	/**
	 * 根据查询条件查询列表
	 */
	List<UserInfo> queryList(UserInfoQuery query);

	/**
	 * 根据查询条件查询数量
	 */
	Integer queryCount(UserInfoQuery query);

	/**
	 * 分页查询
	 */
	PaginationResultVO<UserInfo> queryPage(UserInfoQuery query);

	/**
	 * 新增
	 */
	Integer add(UserInfo bean);

	/**
	 * 批量新增
	 */
	Integer addBatch(List<UserInfo> listBean);

	/**
	 * 批量新增或修改
	 */
	Integer addOrUpdateBatch(List<UserInfo> listBean);

	/**
	 * 根据UserId查询
	 */
	UserInfo getUserInfoByUserId(String userId);


	/**
	 * 根据UserId更新
	 */
	Integer updateUserInfoByUserId(UserInfo bean, String userId);


	/**
	 * 根据UserId删除
	 */
	Integer deleteUserInfoByUserId(String userId);


	/**
	 * 根据Email查询
	 */
	UserInfo getUserInfoByEmail(String email);


	/**
	 * 根据Email更新
	 */
	Integer updateUserInfoByEmail(UserInfo bean, String email);


	/**
	 * 根据Email删除
	 */
	Integer deleteUserInfoByEmail(String email);


	/**
	 * 根据Nickname查询
	 */
	UserInfo getUserInfoByNickname(String nickname);


	/**
	 * 根据Nickname更新
	 */
	Integer updateUserInfoByNickname(UserInfo bean, String nickname);


	/**
	 * 根据Nickname删除
	 */
	Integer deleteUserInfoByNickname(String nickname);


	void register(String email, String nickname, String password) throws BusinessException;

	TokenUserInfoDto login(String email, String password, String ip) throws BusinessException;
}