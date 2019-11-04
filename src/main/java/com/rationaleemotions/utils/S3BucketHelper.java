package com.rationaleemotions.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PublicAccessBlockConfiguration;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.SetPublicAccessBlockRequest;
import com.rationaleemotions.Constants;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class S3BucketHelper {

  private final Context context;

  public S3BucketHelper(Context context) {
    this.context = context;
  }

  public void uploadFileToBucket(String bucketName, String key, File file) {
    AmazonS3 s3client = getClient();
    if (!s3client.doesBucketExistV2(bucketName)) {
      //Create bucket if it doesn't exist.
      CreateBucketRequest rqst = new CreateBucketRequest(bucketName)
          .withObjectLockEnabledForBucket(true)
          .withCannedAcl(CannedAccessControlList.Private);
      s3client.createBucket(rqst);
      //Restrict its access to only the owner and disable public access
      PublicAccessBlockConfiguration cfg = new PublicAccessBlockConfiguration()
          .withBlockPublicAcls(true)
          .withIgnorePublicAcls(true)
          .withBlockPublicPolicy(true)
          .withRestrictPublicBuckets(true);
      SetPublicAccessBlockRequest accessRqst = new SetPublicAccessBlockRequest()
          .withBucketName(bucketName)
          .withPublicAccessBlockConfiguration(cfg);
      s3client.setPublicAccessBlock(accessRqst);
    }
    ObjectListing objects = s3client.listObjects(bucketName);
    objects.getObjectSummaries()
        .forEach(s3ObjectSummary -> s3client.deleteObject(bucketName, s3ObjectSummary.getKey()));
    s3client.putObject(bucketName, key, file);
  }

  public List<String> readValuesFromBucket(String bucketName, String key) {
    AmazonS3 s3client = getClient();
    try (final S3Object s3Object = s3client.getObject(bucketName, key);
        final InputStreamReader streamReader = new InputStreamReader(s3Object.getObjectContent(),
            StandardCharsets.UTF_8);
        final BufferedReader reader = new BufferedReader(streamReader)) {
      return reader.lines().collect(Collectors.toList());
    } catch (final IOException e) {
      return new ArrayList<>();
    }

  }

  private AmazonS3 getClient() {
    context.getLogger().log("Instantiating a S3Client with implicit credentials.");
    return AmazonS3ClientBuilder
        .standard()
        .withRegion(Constants.DEFAULT_REGION)
        .build();

  }

}
