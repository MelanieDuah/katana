package com.katana.infrastructure.support;

/**
 * Created by Akwasi Owusu on 1/24/18
 */

public interface ActionFuncWithOneParam extends Action {
    <T> void invoke(T param);
}
