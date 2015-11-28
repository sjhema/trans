package com.primovision.lutransport.model;

import java.io.Serializable;
import java.util.Date;

public interface BaseModel extends Serializable {

    Long getId();

    void setId(Long id);

    Long getCreatedBy();

    void setCreatedBy(Long createdBy);

    Long getModifiedBy();

    void setModifiedBy(Long modifiedBy);

    Date getCreatedAt();

    void setCreatedAt(Date createdAt);

    Date getModifiedAt();

    void setModifiedAt(Date modifiedAt);

    Integer getStatus();

    void setStatus(Integer status);

}
