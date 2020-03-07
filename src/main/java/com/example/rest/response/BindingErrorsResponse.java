package com.example.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

public class BindingErrorsResponse {

    public BindingErrorsResponse() {
        this(null);
    }

    public BindingErrorsResponse(Integer id) {
        this(null, id);
    }

    public BindingErrorsResponse(Integer pathId, Integer bodyId) {
        boolean onlyBodyIdSpecified = pathId == null && bodyId != null;
        if (onlyBodyIdSpecified) {
            addBodyIdError(bodyId, "must not be specified");
        }
        boolean bothIdSpecified = pathId != null && bodyId != null;
        if (bothIdSpecified && !pathId.equals(bodyId)) {
            addBodyIdError(bodyId, String.format("does not match pathId: %d", pathId));
        }
    }

    private void addBodyIdError(Integer bodyId, String message) {
        BindingError error = new BindingError();
        error.setObjectName("body");
        error.setFieldName("id");
        error.setFieldValue(bodyId.toString());
        error.setErrorMessage(message);
        addError(error);
    }

    public void addAllErrors(BindingResult bindingResult) {
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            BindingError error = new BindingError();
            error.setObjectName(fieldError.getObjectName());
            error.setFieldName(fieldError.getField());
            error.setFieldValue(fieldError.getRejectedValue().toString());
            error.setErrorMessage(fieldError.getDefaultMessage());
            addError(error);
        }
    }

    private List<BindingError> bindingErrors = new ArrayList<>();

    public void addError(BindingError error) {
        this.bindingErrors.add(error);
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, ANY);
        String errorsAsJson = "";
        try {
            errorsAsJson = objectMapper.writeValueAsString(bindingErrors);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return errorsAsJson;
    }

    @Override
    public String toString() {
        return "BindingErrorsResponse [bindingErrors=" + bindingErrors + "]";
    }

    @Getter
    @Setter
    @ToString
    static class BindingError {

        private String objectName;
        private String fieldName;
        private String fieldValue;
        private String errorMessage;

        public BindingError() {
            this.objectName = "";
            this.fieldName = "";
            this.fieldValue = "";
            this.errorMessage = "";
        }
    }
}
