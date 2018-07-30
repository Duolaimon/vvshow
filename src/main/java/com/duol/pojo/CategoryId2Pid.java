package com.duol.pojo;

/**
 * @author Duolaimon
 * 18-7-23 上午11:14
 */
public class CategoryId2Pid {
    private Integer id;
    private Integer parentId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public CategoryId2Pid() {

    }

    public CategoryId2Pid(Integer id, Integer parentId) {
        this.id = id;
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "CategoryId2Pid{" +
                "id=" + id +
                ", parentId=" + parentId +
                '}';
    }
}
