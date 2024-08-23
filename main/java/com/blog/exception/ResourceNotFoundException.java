package com.blog.exception;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.StandardException;

import java.util.function.Supplier;

@Getter
@Setter
@NoArgsConstructor
@StandardException
public class ResourceNotFoundException extends RuntimeException {
    String resourceName;
    String fieldName;
    String resourceId;


    public ResourceNotFoundException(String resourceName, String fieldName, String resourceId) {
        super(String.format("%s Not Found with %s : %s ",resourceName,fieldName, resourceId));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.resourceId = resourceId;
    }
}
