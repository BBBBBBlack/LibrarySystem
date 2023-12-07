package com.bbbbbblack.configuration;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AliPayConfig {
//    @Value("${alipay.appId}")
    private String appId;
//    @Value("${alipay.appPrivateKey}")
    private String appPrivateKey;
//    @Value("${alipay.appCertPublicPath}")
    private String appCertPublicPath;
//    @Value("${alipay.alipayCertPublicPath}")
    private String alipayCertPublicPath;
//    @Value("${alipay.alipayRootCertPath}")
    private String alipayRootCertPath;
    @Bean
    public AlipayClient init() throws AlipayApiException {
        CertAlipayRequest certAlipayRequest=new CertAlipayRequest();
        certAlipayRequest.setPrivateKey(appPrivateKey);
        certAlipayRequest.setServerUrl("https://openapi.alipaydev.com/gateway.do");
        certAlipayRequest.setAppId(appId);
        certAlipayRequest.setCharset("UTF8");
        certAlipayRequest.setSignType("RSA2");
        certAlipayRequest.setEncryptor("");
        certAlipayRequest.setFormat("json");
        certAlipayRequest.setCertPath(appCertPublicPath);
        certAlipayRequest.setAlipayPublicCertPath(alipayCertPublicPath);
        certAlipayRequest.setRootCertPath(alipayRootCertPath);
        return new DefaultAlipayClient(certAlipayRequest);
    }
}
