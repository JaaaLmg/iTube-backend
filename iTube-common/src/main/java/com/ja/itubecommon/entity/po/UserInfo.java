package com.ja.itubecommon.entity.po;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import com.ja.itubecommon.utils.DateUtils;
import com.ja.itubecommon.entity.enums.DateTimePatternEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @Description 用户表
 * @Author LumingJia
 * @Date 2026/03/25
 */
public class UserInfo implements Serializable {
	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 昵称
	 */
	private String nickname;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 性别（0：女，1：男，2：未知）
	 */
	private Integer sex;

	/**
	 * 生日
	 */
	private String birthday;

	/**
	 * 学校
	 */
	private String school;

	/**
	 * 个人简介
	 */
	private String personalIntroduction;

	/**
	 * 加入时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date joinTime;

	/**
	 * 最后登录时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastLoginTime;

	/**
	 * 最后登录ip
	 */
	private String lastLoginIp;

	/**
	 * 账号状态（0：禁用，1：正常）
	 */
	@JsonIgnore
	private Integer status;

	/**
	 * 空间公告
	 */
	private String noticeInfo;

	/**
	 * 历史硬币总数
	 */
	private Integer totalCoinCount;

	/**
	 * 当前硬币数
	 */
	private Integer currentCoinCount;

	/**
	 * 主题编号
	 */
	private Integer theme;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}
	public String getPersonalIntroduction() {
		return personalIntroduction;
	}

	public void setPersonalIntroduction(String personalIntroduction) {
		this.personalIntroduction = personalIntroduction;
	}
	public Date getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getNoticeInfo() {
		return noticeInfo;
	}

	public void setNoticeInfo(String noticeInfo) {
		this.noticeInfo = noticeInfo;
	}
	public Integer getTotalCoinCount() {
		return totalCoinCount;
	}

	public void setTotalCoinCount(Integer totalCoinCount) {
		this.totalCoinCount = totalCoinCount;
	}
	public Integer getCurrentCoinCount() {
		return currentCoinCount;
	}

	public void setCurrentCoinCount(Integer currentCoinCount) {
		this.currentCoinCount = currentCoinCount;
	}
	public Integer getTheme() {
		return theme;
	}

	public void setTheme(Integer theme) {
		this.theme = theme;
	}
	@Override
	public String toString() {
		return "用户表 [" + " 用户id:" + (userId == null ? "空" : userId) + " 昵称:" + (nickname == null ? "空" : nickname) + " 邮箱:" + (email == null ? "空" : email) + " 密码:" + (password == null ? "空" : password) + " 性别（0：女，1：男，2：未知）:" + (sex == null ? "空" : sex) + " 生日:" + (birthday == null ? "空" : birthday) + " 学校:" + (school == null ? "空" : school) + " 个人简介:" + (personalIntroduction == null ? "空" : personalIntroduction) + " 加入时间:" + (joinTime == null ? "空" : DateUtils.format(joinTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + " 最后登录时间:" + (lastLoginTime == null ? "空" : DateUtils.format(lastLoginTime, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + " 最后登录ip:" + (lastLoginIp == null ? "空" : lastLoginIp) + " 账号状态（0：禁用，1：正常）:" + (status == null ? "空" : status) + " 空间公告:" + (noticeInfo == null ? "空" : noticeInfo) + " 历史硬币总数:" + (totalCoinCount == null ? "空" : totalCoinCount) + " 当前硬币数:" + (currentCoinCount == null ? "空" : currentCoinCount) + " 主题编号:" + (theme == null ? "空" : theme) + " ]";
	}
}