package yong.entity;

import yong.common.ObjectMapperUtils;

public class BaseEntity {

    public <T> T toDto(Class<T> type) {
        return ObjectMapperUtils.map(this, type);
    }
}
