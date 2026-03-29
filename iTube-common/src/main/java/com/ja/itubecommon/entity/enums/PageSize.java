package com.ja.itubecommon.entity.enums;
public enum PageSize {
    SIZE10(10),
    SIZE20(20),
    SIZE30(30),
    SIZE40(40),
    SIZE50(50),
    SIZE60(60),
    SIZE70(70),
    SIZE80(80),
    SIZE90(90),
    SIZE100(100);

    int size;

    private PageSize(int size){
        this.size = size;
    }

    public int getSize(){
        return size;
    }
}
