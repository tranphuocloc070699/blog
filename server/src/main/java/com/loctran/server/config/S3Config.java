package com.loctran.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {
  private final String region = "ca-central-1";
  private final String ACCESS_KEY = "AKIASE47AYPKPD5E3JSE";
  private final String SECRET_KEY="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";
  @Bean
  public S3Client s3Client() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY);
    return S3Client.builder()
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .build();
  }
}
