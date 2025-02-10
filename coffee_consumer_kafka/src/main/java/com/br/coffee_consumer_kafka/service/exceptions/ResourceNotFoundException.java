package com.br.coffee_consumer_kafka.service.exceptions;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(Object resource) {
        super("Resource not found: " + resource.toString());
    }
}
