package com.ss.load.balancer.request;

public interface Request {

    void respond(int httpStatus);
}
