package com.ja.itubecommon.service.impl;

import com.ja.itubecommon.entity.constants.Constants;
import com.ja.itubecommon.entity.enums.UserSexEnum;
import com.ja.itubecommon.entity.enums.UserStatusEnum;
import com.ja.itubecommon.exception.BusinessException;
import com.ja.itubecommon.mappers.UserInfoMapper;
import com.ja.itubecommon.service.UserInfoService;
import com.ja.itubecommon.entity.po.UserInfo;
import com.ja.itubecommon.entity.query.UserInfoQuery;
import com.ja.itubecommon.entity.query.SimplePage;
import com.ja.itubecommon.entity.enums.PageSize;
import com.ja.itubecommon.entity.vo.PaginationResultVO;
import com.ja.itubecommon.utils.StringTools;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description 用户表业务功能实现类
 * @Author LumingJia
 * @Date 2026/03/25
 */
@Service("UserInfoService")
public class UserInfoServiceImpl implements UserInfoService {
	@Resource
	private UserInfoMapper<UserInfo,UserInfoQuery> userInfoMapper;

	/**
	 * 根据查询条件查询列表
	 */
	@Override
	public List<UserInfo> queryList(UserInfoQuery query) {
		 return this.userInfoMapper.selectList(query);
	}

	/**
	 * 根据查询条件查询数量
	 */
	@Override
	public Integer queryCount(UserInfoQuery query) {
		 return this.userInfoMapper.selectCount(query);
	}

	/**
	 * 分页查询
	 */
	@Override
	public PaginationResultVO<UserInfo> queryPage(UserInfoQuery query) {
		Integer count = this.queryCount(query);
		Integer pageSize = query.getPageSize()==null?PageSize.SIZE20.getSize():query.getPageSize();
		SimplePage page = new SimplePage(query.getPageNo(), count, pageSize);
		query.setSimplePage(page);
		List<UserInfo> list = this.queryList(query);
		PaginationResultVO<UserInfo> result = new PaginationResultVO<UserInfo>(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(UserInfo bean) {
		return this.userInfoMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<UserInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<UserInfo> listBean) {
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.userInfoMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据UserId查询
	 */
	@Override
	public UserInfo getUserInfoByUserId(String userId) {
		return this.userInfoMapper.selectByUserId(userId);
	}

	/**
	 * 根据UserId更新
	 */
	@Override
	public Integer updateUserInfoByUserId(UserInfo bean, String userId) {
		return this.userInfoMapper.updateByUserId(bean, userId);
	}

	/**
	 * 根据UserId删除
	 */
	@Override
	public Integer deleteUserInfoByUserId(String userId) {
		return this.userInfoMapper.deleteByUserId(userId);
	}

	/**
	 * 根据Email查询
	 */
	@Override
	public UserInfo getUserInfoByEmail(String email) {
		return this.userInfoMapper.selectByEmail(email);
	}

	/**
	 * 根据Email更新
	 */
	@Override
	public Integer updateUserInfoByEmail(UserInfo bean, String email) {
		return this.userInfoMapper.updateByEmail(bean, email);
	}

	/**
	 * 根据Email删除
	 */
	@Override
	public Integer deleteUserInfoByEmail(String email) {
		return this.userInfoMapper.deleteByEmail(email);
	}

	/**
	 * 根据Nickname查询
	 */
	@Override
	public UserInfo getUserInfoByNickname(String nickname) {
		return this.userInfoMapper.selectByNickname(nickname);
	}

	/**
	 * 根据Nickname更新
	 */
	@Override
	public Integer updateUserInfoByNickname(UserInfo bean, String nickname) {
		return this.userInfoMapper.updateByNickname(bean, nickname);
	}

	/**
	 * 根据Nickname删除
	 */
	@Override
	public Integer deleteUserInfoByNickname(String nickname) {
		return this.userInfoMapper.deleteByNickname(nickname);
	}

	@Override
	public void register(String email, String nickname, String password) throws BusinessException {
		UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
		if(null != userInfo) {
			throw new BusinessException("邮箱已存在");
		}
		UserInfo nicknameUserInfo = this.userInfoMapper.selectByNickname(nickname);
		if(null != nicknameUserInfo) {
			throw new BusinessException("昵称已存在");
		}
		userInfo = new UserInfo();
		String userId = StringTools.getRandomNumber(Constants.LENGTH_USER_ID);
		userInfo.setUserId(userId);
		userInfo.setEmail(email);
		userInfo.setNickname(nickname);
		userInfo.setPassword(StringTools.encodeByMd5(password));
		userInfo.setJoinTime(new Date());
		userInfo.setStatus(UserStatusEnum.ENABLED.getStatus());
		userInfo.setSex(UserSexEnum.UNKNOWN.getType());
		userInfo.setTheme(1);
		userInfo.setCurrentCoinCount(10);
		userInfo.setTotalCoinCount(10);

		this.userInfoMapper.insert(userInfo);
	}

}