package com.ja.itubecommon.entity.query;

import java.util.Date;

/**
 * @Description 用户表查询对象
 * @Author LumingJia
 * @Date 2026/03/25
 */
public class InfoQuery extends BaseQuery {
	/**
	 * 用户id
	 */
	private String userId;
	private String userIdFuzzy;

	/**
	 * 昵称
	 */
	private String nickname;
	private String nicknameFuzzy;

	/**
	 * 邮箱
	 */
	private String email;
	private String emailFuzzy;

	/**
	 * 密码
	 */
	private String password;
	private String passwordFuzzy;

	/**
	 * 性别（0：女，1：男，2：未知）
	 */
	private Integer sex;

	/**
	 * 生日
	 */
	private String birthday;
	private String birthdayFuzzy;

	/**
	 * 学校
	 */
	private String school;
	private String schoolFuzzy;

	/**
	 * 个人简介
	 */
	private String personalIntroduction;
	private String personalIntroductionFuzzy;

	/**
	 * 加入时间
	 */
	private Date joinTime;
	private String joinTimeStart;
	private String joinTimeEnd;

	/**
	 * 最后登录时间
	 */
	private Date lastLoginTime;
	private String lastLoginTimeStart;
	private String lastLoginTimeEnd;

	/**
	 * 最后登录ip
	 */
	private String lastLoginIp;
	private String lastLoginIpFuzzy;

	/**
	 * 账号状态（0：禁用，1：正常）
	 */
	private Integer status;

	/**
	 * 空间公告
	 */
	private String noticeInfo;
	private String noticeInfoFuzzy;

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
	public String getUserIdFuzzy() {
		return userIdFuzzy;
	}

	public void setUserIdFuzzy(String userIdFuzzy) {
		this.userIdFuzzy = userIdFuzzy;
	}
	public String getNicknameFuzzy() {
		return nicknameFuzzy;
	}

	public void setNicknameFuzzy(String nicknameFuzzy) {
		this.nicknameFuzzy = nicknameFuzzy;
	}
	public String getEmailFuzzy() {
		return emailFuzzy;
	}

	public void setEmailFuzzy(String emailFuzzy) {
		this.emailFuzzy = emailFuzzy;
	}
	public String getPasswordFuzzy() {
		return passwordFuzzy;
	}

	public void setPasswordFuzzy(String passwordFuzzy) {
		this.passwordFuzzy = passwordFuzzy;
	}
	public String getBirthdayFuzzy() {
		return birthdayFuzzy;
	}

	public void setBirthdayFuzzy(String birthdayFuzzy) {
		this.birthdayFuzzy = birthdayFuzzy;
	}
	public String getSchoolFuzzy() {
		return schoolFuzzy;
	}

	public void setSchoolFuzzy(String schoolFuzzy) {
		this.schoolFuzzy = schoolFuzzy;
	}
	public String getPersonalIntroductionFuzzy() {
		return personalIntroductionFuzzy;
	}

	public void setPersonalIntroductionFuzzy(String personalIntroductionFuzzy) {
		this.personalIntroductionFuzzy = personalIntroductionFuzzy;
	}
	public String getJoinTimeStart() {
		return joinTimeStart;
	}

	public void setJoinTimeStart(String joinTimeStart) {
		this.joinTimeStart = joinTimeStart;
	}
	public String getJoinTimeEnd() {
		return joinTimeEnd;
	}

	public void setJoinTimeEnd(String joinTimeEnd) {
		this.joinTimeEnd = joinTimeEnd;
	}
	public String getLastLoginTimeStart() {
		return lastLoginTimeStart;
	}

	public void setLastLoginTimeStart(String lastLoginTimeStart) {
		this.lastLoginTimeStart = lastLoginTimeStart;
	}
	public String getLastLoginTimeEnd() {
		return lastLoginTimeEnd;
	}

	public void setLastLoginTimeEnd(String lastLoginTimeEnd) {
		this.lastLoginTimeEnd = lastLoginTimeEnd;
	}
	public String getLastLoginIpFuzzy() {
		return lastLoginIpFuzzy;
	}

	public void setLastLoginIpFuzzy(String lastLoginIpFuzzy) {
		this.lastLoginIpFuzzy = lastLoginIpFuzzy;
	}
	public String getNoticeInfoFuzzy() {
		return noticeInfoFuzzy;
	}

	public void setNoticeInfoFuzzy(String noticeInfoFuzzy) {
		this.noticeInfoFuzzy = noticeInfoFuzzy;
	}
}