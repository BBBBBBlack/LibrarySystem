package com.bbbbbblack.configuration.esConfiguration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;


public class EsClientPoolFactory implements PooledObjectFactory<ElasticsearchClient> {


    @Override
    public PooledObject<ElasticsearchClient> makeObject() throws Exception {
//         创建低级客户端
        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "r3mHUR3J-nYuvSkATWgl"));

        String keyStorePass = "password";
        KeyStore truststore = KeyStore.getInstance("pkcs12");
        try (InputStream is = Files.newInputStream(Paths.get("C:\\Users\\black\\Downloads\\truststore.p12"))) {
            System.out.println(is);
            truststore.load(is, keyStorePass.toCharArray());
        }
        SSLContextBuilder sslBuilder = SSLContexts.custom()
                .loadTrustMaterial(truststore, null);
        final SSLContext sslContext = sslBuilder.build();
        RestClient restClient = RestClient.builder(
                        new HttpHost("192.168.147.128", 9200, "https"))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setSSLContext(sslContext)
                        .setDefaultCredentialsProvider(credentialsProvider))
                .build();
        //使用Jackson映射器创建传输层
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper()
        );

        ElasticsearchClient client = new ElasticsearchClient(transport);
        //log.info("对象被创建了" + client);
        return new DefaultPooledObject<>(client);
    }

    @Override
    public void destroyObject(PooledObject<ElasticsearchClient> p) throws Exception {
        ElasticsearchClient elasticsearchClient = p.getObject();
        //log.info("对象被销毁了" + elasticsearchClient);
    }

    @Override
    public boolean validateObject(PooledObject<ElasticsearchClient> p) {
        return true;
    }

    @Override
    public void activateObject(PooledObject<ElasticsearchClient> p) throws Exception {
        //log.info("对象被激活了" + p.getObject());
    }

    @Override
    public void passivateObject(PooledObject<ElasticsearchClient> p) throws Exception {
        //log.info("对象被钝化了" + p.getObject());
    }
}
