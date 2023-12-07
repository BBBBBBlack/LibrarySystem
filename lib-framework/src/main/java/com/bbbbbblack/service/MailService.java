package com.bbbbbblack.service;

import com.bbbbbblack.domain.Result;
import com.bbbbbblack.domain.entity.Mail;

public interface MailService {
    Result sendMail(String email,Integer type);
}
