package com.blog.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY) // 비어있는 json 일 경우, 해당 field 는 제외 함
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> validateProp;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validateProp) {
        this.code = code;
        this.message = message;
        this.validateProp = validateProp != null ? validateProp : new HashMap<>();
    }

    public void addValidation(String field, String message) {
        this.validateProp.put(field, message);
    }

}
