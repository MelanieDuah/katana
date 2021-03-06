package com.katana.entities;

import java.util.Date;

/**
 * The Category entity
 *
 * @author Akwasi Owusu
 */

public class Category extends BaseEntity {

    private static final long serialVersionUID = -3981110032326693701L;
    private String categoryName;
    public Date lastModified;

    public Category() {
        super();
    }

    public Category(String categoryName) {
        super();
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public String toString() {
        return categoryName;
    }

    @Override
    public boolean equals(Object another) {
        boolean equal = false;

        if (this == another) {
            equal = true;
        } else if (another != null && another instanceof Category) {
            Category anotherCategory = (Category) another;
            equal = anotherCategory.categoryName != null && anotherCategory.categoryName.equals(categoryName);
        }
        return equal;
    }

    @Override
    public int hashCode() {
        return 31 * (categoryName != null ? categoryName.hashCode() : super.hashCode());
    }

}
