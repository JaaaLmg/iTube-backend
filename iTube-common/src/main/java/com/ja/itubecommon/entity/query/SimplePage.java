package com.ja.itubecommon.entity.query;
import com.ja.itubecommon.entity.enums.PageSize;
public class SimplePage {
    /**
     * 当前页码
     */
    private int pageNo;

    /**
     * 总记录数
     */
    private int countTotal;

    /**
     * 每页大小
     */
    private int pageSize;

    /**
     * 总页数
     */
    private int pageTotal;

    /**
     * 起始索引
     */
    private int start;

    /**
     * 偏移量
     */
    private int offset;


    public SimplePage(){};

    public SimplePage(Integer pageNo, int countTotal, int pageSize){
        if(null == pageNo){
            pageNo = 0;
        }
        this.pageNo = pageNo;
        this.countTotal = countTotal;
        this.pageSize = pageSize;
        action();
    }

    public SimplePage(int start, int offset){
        this.start = start;
        this.offset = offset;
    }

    public void action(){
        if(this.pageSize <= 0){
            this.pageSize = PageSize.SIZE20.getSize();
        }
        if(this.countTotal > 0){
            this.pageTotal = this.countTotal % this.pageSize == 0 ? this.countTotal/this.pageSize : this.countTotal/this.pageSize+1;
        }else{
            pageTotal = 1;
        }

        if(pageNo <= 1){
            pageNo = 1;
        }
        if(pageNo > pageTotal){
            pageNo = pageTotal;
        }
        this.start = (pageNo-1)*pageSize;
        this.offset = this.pageSize;
    }


    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getCountTotal() {
        return countTotal;
    }

    public void setCountTotal(int countTotal) {
        this.countTotal = countTotal;
        action();
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
