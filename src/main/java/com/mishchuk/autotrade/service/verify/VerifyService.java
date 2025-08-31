package com.mishchuk.autotrade.service.verify;

public interface VerifyService {
    void start(String destination, Channel channel);
    boolean check(String destination, String code);
    enum Channel { SMS, EMAIL }
}