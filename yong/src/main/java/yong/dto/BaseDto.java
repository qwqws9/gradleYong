package yong.dto;

import yong.common.ObjectMapperUtils;

public class BaseDto {

    /**
     * 현재 객체의 정보를 대상 클래스로 변환해서 반환함.
     * @param <T>
     * @param type
     * @return
     */
    public <T> T toEntity(Class<T> type) {
        return ObjectMapperUtils.map(this, type);
    }
}
