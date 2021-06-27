package com.ss.load.balancer.request.impl;

import com.ss.load.balancer.request.Request;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimpleRequest implements Request {

    @Getter int respond;

    @Override
    public void respond(int httpStatus) {

        respond = httpStatus;
    }
}
