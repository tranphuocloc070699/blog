package com.loctran.server.admin.service;

import com.loctran.server.exception.custom.ConflictException;
import com.loctran.server.exception.custom.NotFoundException;
import com.loctran.server.shared.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileService {
  @Value("${application.bucket.name}")
  private String bucketName;
  
  private final S3Client s3Client;
  private final HttpServletRequest httpServletRequest;
  
  public ResponseObject uploadImage(MultipartFile file)  {
    if(file==null){
      throw new ConflictException("file not found", httpServletRequest.getServletPath());
    }
    
    String filename =   UUID.randomUUID() + "-" + file.getOriginalFilename() ;
  
    String objectKey = "story/" + filename;
    PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucketName)
            .contentType("image/" + "jpeg")
            .key(objectKey)
            .build();
    try {
      s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
    } catch (IOException e) {
      throw new ConflictException(e.getMessage(), httpServletRequest.getServletPath());
    }
  
    String url =s3Client.utilities().getUrl(GetUrlRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .build()).toExternalForm();
    Map<String,String> data = new HashMap<>();
    data.put("url",url);
    return ResponseObject.builder()
            .timestamp(new Date())
            .message("upload image to S3 successfully!")
            .status(HttpStatus.OK)
            .path(httpServletRequest.getServletPath())
            .data(data)
            .build();
  }
  
  public ResponseObject deleteImage(String url) {
    HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
            .bucket(bucketName)
            .key(extractObjectKey(url))
            .build();
    DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
            .bucket(bucketName)
            .key( extractObjectKey(url))
            .build();

    try {
      s3Client.headObject(headObjectRequest);
       s3Client.deleteObject(deleteRequest);
      Map<String,String> data = new HashMap<>();
      data.put("url",url);
    
      return ResponseObject.builder()
              .timestamp(new Date())
              .message("delete image to S3 successfully!")
              .status(HttpStatus.OK)
              .path(httpServletRequest.getServletPath())
              .data(data)
              .build();
    } catch (NoSuchKeyException e) {
      System.err.println(e.getMessage());
      throw new NotFoundException("image url not found to delete", httpServletRequest.getServletPath());
    } catch (S3Exception e) {
      System.err.println("Error deleting object: " + e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }
  
  private String extractObjectKey(String url){
    String urlWithoutPrefix = url.replace("https://", "");
    String[] parts = urlWithoutPrefix.split("/");
    String bucketName = parts[0];
    return urlWithoutPrefix.substring(bucketName.length() + 1);
   
  }
}
