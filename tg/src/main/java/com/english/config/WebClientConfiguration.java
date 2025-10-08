package com.english.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.security.KeyStore;

@Configuration
public class WebClientConfiguration {
    @Value("${server.learn.url}")
    private String baseUrl;

    @Bean
    public WebClient webClient(
            @Value("${ssl.keystore.path}") String keystorePath,
            @Value("${ssl.keystore.password}") String keystorePassword,
            @Value("${ssl.key.store.type}") String keystoreType) {

        try {
            // Загружаем keystore из файла
            KeyStore keyStore = KeyStore.getInstance(keystoreType);
            InputStream keyStoreStream = new ClassPathResource(keystorePath).getInputStream();
            keyStore.load(keyStoreStream, keystorePassword.toCharArray());

            // Создаем TrustManagerFactory с нашим keystore
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            // Создаем SSL контекст
            SslContext sslContext = SslContextBuilder.forClient()
                    .trustManager(trustManagerFactory)
                    .build();

            HttpClient httpClient = HttpClient.create()
                    .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));

            return WebClient.builder()
                    .baseUrl(baseUrl)
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to configure SSL context with PKCS12 keystore", e);
        }
    }
}
