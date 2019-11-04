package com.rationaleemotions.lambdas;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.rationaleemotions.Constants;
import com.rationaleemotions.utils.S3BucketHelper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.UUID;

public class UploadDocSetsToS3BucketLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {

    File file;
    try {
      file = createDocSetIdFile();
      FileOutputStream fos = new FileOutputStream(file);

      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
      for (int i = 0; i < 100; i++) {
        bw.write(UUID.randomUUID().toString());
        bw.newLine();
      }
      bw.close();
      context.getLogger().log("\nCompleted generating information");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    new S3BucketHelper(context).uploadFileToBucket(Constants.S3_BUCKET_NAME, Constants.S3_BUCKET_FILENAME, file);
    context.getLogger().log("\nUploading complete for file " + file.getAbsolutePath());
    return new APIGatewayProxyResponseEvent()
        .withBody("success")
        .withStatusCode(200);
  }

  private File createDocSetIdFile() throws IOException {
    return File.createTempFile(Long.toString(System.currentTimeMillis()), ".txt");
  }
}
