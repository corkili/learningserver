package com.corkili.learningserver.po;

import java.util.Date;

public interface PersistObject {

    void setId(Long id);

    Long getId();

    void setCreateTime(Date createTime);

    Date getCreateTime();

    void setUpdateTime(Date updateTime);

    Date getUpdateTime();
}
