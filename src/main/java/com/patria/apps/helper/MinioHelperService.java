package com.patria.apps.helper;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class MinioHelperService {

    private final MinioClient minioClient;
    private final int defaultExpiry;

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.part-size}")
    private Long partSize;

    public MinioHelperService(MinioClient minioClient) {
        this.minioClient = minioClient;
        this.defaultExpiry = 1;
    }

    public String getPresignedObjectUrl(String path, Optional<Integer> expiry, Optional<TimeUnit> timeUnit) throws Exception {
        int expireTime = expiry.orElse(this.defaultExpiry);
        TimeUnit unitTime = timeUnit.orElse(TimeUnit.DAYS);
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(this.bucket)
                        .object(path)
                        .expiry(expireTime, unitTime)
                        .build()
        );
    }

    public GetObjectResponse getObject(String path) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(this.bucket)
                        .object(path)
                        .build()
        );
    }

    public ObjectWriteResponse putObject(MultipartFile file, String path, String contentType) throws Exception {
        return minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(this.bucket)
                        .object(path)
                        .contentType(contentType)
                        .stream(
                                file.getInputStream(),
                                file.getSize(),
                                this.partSize
                        )
                        .build()
        );
    }

    public ObjectWriteResponse putObject(InputStream inputStream, String path, String contentType, long fileSize) throws Exception {
        return minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(this.bucket)
                        .object(path)
                        .contentType(contentType)
                        .stream(
                                inputStream,
                                fileSize,
                                this.partSize
                        )
                        .build()
        );
    }

    public void removeObject(String path) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(this.bucket)
                        .object(path)
                        .build()
        );
    }

    public void removeFolder(String folderPath) throws Exception {
        String prefix = folderPath.endsWith("/") ? folderPath : folderPath + "/";

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(this.bucket)
                        .prefix(prefix)
                        .recursive(true)
                        .build()
        );

        List<DeleteObject> objectsToDelete = new ArrayList<>();
        List<String> allNames = new ArrayList<>();
        for (Result<Item> r : results) {
            Item item = r.get(); 
            allNames.add(item.objectName());
            objectsToDelete.add(new DeleteObject(item.objectName()));
        }

        Iterable<Result<DeleteError>> errors = minioClient.removeObjects(
                RemoveObjectsArgs.builder()
                        .bucket(this.bucket)
                        .objects(objectsToDelete)
                        .build()
        );

        List<String> failed = new ArrayList<>();
        for (Result<DeleteError> er : errors) {
            try {
                DeleteError de = er.get(); 
                failed.add(de.objectName() + ": " + de.message());
            } catch (Exception e) {
                failed.add("error reading removeObjects result: " + e.getMessage());
            }
        }

        if (!failed.isEmpty()) {
            throw new Exception("Failed to delete some objects: " + failed);
        }
    }

    public List<String> listImageUrlsInFolder(String folderPath) throws Exception {
        return listImageUrlsInFolder(folderPath, Optional.of(defaultExpiry), Optional.of(TimeUnit.DAYS));
    }

    public List<String> listImageUrlsInFolder(String folderPath, Optional<Integer> expiry, Optional<TimeUnit> timeUnit) throws Exception {
        List<String> urls = new ArrayList<>();

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(this.bucket)
                        .prefix(folderPath.endsWith("/") ? folderPath : folderPath + "/")
                        .recursive(true)
                        .build()
        );

        for (Result<Item> result : results) {
            Item item = result.get();
            String objectName = item.objectName();

            if (objectName.endsWith(".jpg") || objectName.endsWith(".jpeg")
                    || objectName.endsWith(".png") || objectName.endsWith(".gif")
                    || objectName.endsWith(".webp")) {

                urls.add(getPresignedObjectUrl(objectName, expiry, timeUnit));
            }
        }
        return urls;
    }
}
