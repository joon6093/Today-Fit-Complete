package com.SJY.TodayFitComplete_Backend.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String resourceName; // 찾을 수 없는 리소스 이름
    private String fieldName; // 필드 이름
    private String fieldValue; // 필드 값

    /**
     * ResourceNotFoundException 생성자.
     *
     * @param resourceName 리소스 이름
     * @param fieldName 필드 이름
     * @param fieldValue 필드 값
     */
    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found [%s: %s]", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}

