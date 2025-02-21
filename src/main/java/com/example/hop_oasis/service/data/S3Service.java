package com.example.hop_oasis.service.data;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import java.io.IOException;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public String uploadFile(String keyName, MultipartFile file) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        metadata.setCacheControl("public, max-age=864000");
        var putObjectResult = s3client.putObject(bucketName, keyName, file.getInputStream(), metadata);
        log.info(String.valueOf(putObjectResult.getMetadata()));
        return keyName;
    }

    public S3Object getFile(String keyName) {
        return s3client.getObject(bucketName, keyName);
    }

    public void deleteFile(String keyName) {
        s3client.deleteObject(bucketName, keyName);
    }

    public URL getFileUrl(String fileName) {
        return s3client.getUrl(bucketName, fileName);
    }

}
