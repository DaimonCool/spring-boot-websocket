package com.dashko.spring.ws.api.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class S3Service {

    private AmazonS3Client s3Client;
    private static final String BUCKET_NAME = "daimon-bucket";

    @PostConstruct
    public void initS3Client() {
        val clientRegion = Regions.US_EAST_2;

        val awsCredentials = new BasicAWSCredentials("AKIAJKJMMSN55TF757BQ", "6iriXMlmPaNkCEmolu4uBlQtfUAeEI/Mt8LiYYHx");
        this.s3Client = (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(clientRegion)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public String uploadFile(MultipartFile multipartFile) throws IOException {
        val metadata = new ObjectMetadata();
        metadata.addUserMetadata("content-disposition", "attachment");
        val request = new PutObjectRequest(BUCKET_NAME, multipartFile.getOriginalFilename(),
                new ByteArrayInputStream(multipartFile.getBytes()), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);
        s3Client.putObject(request);
        return s3Client.getResourceUrl(BUCKET_NAME, multipartFile.getOriginalFilename());
    }

}
