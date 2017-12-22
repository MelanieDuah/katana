package com.katana.entities;

import android.support.annotation.NonNull;
import java.util.Date;

/**
 * Created by AOwusu on 11/13/2017.
 */

public abstract class BaseEntity implements Comparable<BaseEntity>{

    private  String id;
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;

        BaseEntity that = (BaseEntity) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compareTo(@NonNull BaseEntity baseEntity) {
        return 0;
    }
}
