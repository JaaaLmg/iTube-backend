package com.ja.itubecommon.entity.enums;

public enum UserSexEnum {
    MALE(1, "男"),
    FEMALE(0, "女"),
    UNKNOWN(2, "未知");

    private Integer type;
    private String desc;

    UserSexEnum(Integer type, String desc){
        this.type = type;
        this.desc = desc;
    }

    public static UserSexEnum getSex(Integer type){
        for(UserSexEnum sexEnum : UserSexEnum.values()){
            if(sexEnum.type.equals(type)){
                return sexEnum;
            }
        }
        return null;
    }

    public Integer getType(){
        return type;
    }

    public String getDesc(){
        return desc;
    }
}
