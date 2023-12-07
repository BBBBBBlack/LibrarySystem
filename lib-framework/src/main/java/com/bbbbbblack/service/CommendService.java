package com.bbbbbblack.service;

import com.bbbbbblack.domain.Result;

public interface CommendService {
    Result openPush(String clientId);
    Result closePush();
    Result openCommend(Integer days);
    Result closeCommend();
}
