package com.rationaleemotions;

import com.amazonaws.regions.Regions;

public interface Constants {

  Regions DEFAULT_REGION = Regions.AP_SOUTH_1;
  String ACCESS_KEY = System.getenv("AWS_ACCESS_KEY_ID");
  String SECRET_KEY = System.getenv("AWS_SECRET_ACCESS_KEY");
  String S3_BUCKET_FILENAME = "docsetfile.txt";
  String S3_BUCKET_NAME = "docsetid";
  String QUEUE_NAME = "docsetQueue.fifo";
  String GROUP_ID = "docsetGroup";
}
