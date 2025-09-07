package com.patria.apps.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfiguration {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.username}")
    private String username;

    @Value("${minio.password}")
    private String password;

    @Value("${minio.bucket}")
    private String bucket;

    @Bean
    public MinioClient client() throws Exception {
        MinioClient client = MinioClient.builder()
                .endpoint(url)
                .credentials(username, password)
                .build();

        client.ignoreCertCheck();

        if (!client.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(this.bucket)
                        .build()
        )) {
            client.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(this.bucket)
                            .build()
            );
        }
        return client;
    }

}
