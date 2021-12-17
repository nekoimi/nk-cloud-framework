package com.nekoimi.nk.auth.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.security.KeyPair;

/**
 * nekoimi  2021/12/17 16:49
 */
@Configuration
public class AuthConfiguration {

    /**
     * key
     * @return
     */
    @Bean
    public KeyPair keyPair(OAuth2ResourceServerProperties properties){
        try {
            File file = ResourceUtils.getFile("classpath:key/jwtkey.jks");
            PathResource resource = new PathResource(file.getAbsolutePath());
            KeyPair keyPair = new KeyStoreKeyFactory(resource, "123456".toCharArray()).getKeyPair("jwtkey");
            return keyPair;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
