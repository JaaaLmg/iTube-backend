package com.ja.itubecommon.entity.enums;

public enum UserStatusEnum {
    DISABLED(0, "禁用"),
    ENABLED(1, "正常");

    private Integer status;
    private String desc;

    UserStatusEnum(Integer status, String desc){
        this.status = status;
        this.desc = desc;
    }

    public static UserStatusEnum getStatus(Integer status){
        for(UserStatusEnum statusEnum : UserStatusEnum.values()){
            if(statusEnum.status.equals(status)){
                return statusEnum;
            }
        }
        return null;
    }

    public Integer getStatus(){
        return status;
    }

    public String getDesc(){
        return desc;
    }
}
