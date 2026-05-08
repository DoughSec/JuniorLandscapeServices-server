package com.landscape.server.security;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CORSConfiguration;
import software.amazon.awssdk.services.s3.model.CORSRule;
import software.amazon.awssdk.services.s3.model.PutBucketCorsRequest;
import software.amazon.awssdk.services.s3.model.PutBucketPolicyRequest;
import software.amazon.awssdk.services.s3.model.PutPublicAccessBlockRequest;
import software.amazon.awssdk.services.s3.model.PublicAccessBlockConfiguration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    private static final String BUCKET_NAME = "juniorlandscape-images";

    @Bean
    public S3Client s3Client() {
        return S3Client.create();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.create();
    }

    // Runs once after all beans are ready. Configures the bucket for:
    //  1. CORS — allows browser PUT requests via presigned URLs
    //  2. Public-read policy — allows <img src> to display uploaded images
    // NOTE: "Block all public access" must be OFF for this bucket in the AWS console.
    @Bean
    public ApplicationRunner configureBucket(S3Client s3Client) {
        return args -> {
            try {
                // 1. Lift the account-level block so a public bucket policy can be applied
                s3Client.putPublicAccessBlock(PutPublicAccessBlockRequest.builder()
                        .bucket(BUCKET_NAME)
                        .publicAccessBlockConfiguration(PublicAccessBlockConfiguration.builder()
                                .blockPublicAcls(false)
                                .ignorePublicAcls(false)
                                .blockPublicPolicy(false)
                                .restrictPublicBuckets(false)
                                .build())
                        .build());

                // 2. Allow <img src> to read uploaded images without signing
                String publicReadPolicy = """
                        {
                            "Version": "2012-10-17",
                            "Statement": [{
                                "Sid": "PublicReadGetObject",
                                "Effect": "Allow",
                                "Principal": "*",
                                "Action": "s3:GetObject",
                                "Resource": "arn:aws:s3:::%s/*"
                            }]
                        }""".formatted(BUCKET_NAME);

                s3Client.putBucketPolicy(PutBucketPolicyRequest.builder()
                        .bucket(BUCKET_NAME)
                        .policy(publicReadPolicy)
                        .build());

                // 3. Allow the browser to PUT directly via presigned URLs
                CORSRule rule = CORSRule.builder()
                        .allowedOrigins("*")
                        .allowedMethods("GET", "PUT", "HEAD")
                        .allowedHeaders("*")
                        .maxAgeSeconds(3000)
                        .build();

                s3Client.putBucketCors(PutBucketCorsRequest.builder()
                        .bucket(BUCKET_NAME)
                        .corsConfiguration(CORSConfiguration.builder().corsRules(rule).build())
                        .build());

            } catch (Exception ignored) {
                // Best-effort — don't fail startup if S3 is unreachable at boot time
            }
        };
    }
}
