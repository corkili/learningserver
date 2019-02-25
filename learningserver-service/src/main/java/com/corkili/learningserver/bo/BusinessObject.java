package com.corkili.learningserver.bo;

import java.util.Date;

public interface BusinessObject {

    void setId(Long id);

    Long getId();

    void setCreateTime(Date createTime);

    Date getCreateTime();

    void setUpdateTime(Date updateTime);

    Date getUpdateTime();

}
