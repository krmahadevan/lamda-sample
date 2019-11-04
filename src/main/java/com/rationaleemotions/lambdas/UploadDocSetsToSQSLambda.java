package com.rationaleemotions.lambdas;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.rationaleemotions.Constants;
import com.rationaleemotions.utils.S3BucketHelper;
import java.util.HashMap;
import java.util.Map;

public class UploadDocSetsToSQSLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  @Override
  public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
    new S3BucketHelper(context).readValuesFromBucket(Constants.S3_BUCKET_NAME, Constants.S3_BUCKET_FILENAME)
        .forEach(msg -> System.err.println(sendMessage(msg, Constants.QUEUE_NAME)));
    return new APIGatewayProxyResponseEvent()
        .withBody("success")
        .withStatusCode(200);
  }

  private String sendMessage(String msg, String q) {
    AmazonSQS sqs = AmazonSQSClientBuilder.standard()
        .withRegion(Constants.DEFAULT_REGION)
        .build();

    final Map<String, String> attributes = new HashMap<>();
    // A FIFO queue must have the FifoQueue attribute set to true.
    attributes.put("FifoQueue", "true");
    //If the user doesn't provide a MessageDeduplicationId, generate a
    // MessageDeduplicationId based on the content.
    attributes.put("ContentBasedDeduplication", "true");

    CreateQueueRequest createQueueRequest = new CreateQueueRequest(q).withAttributes(attributes);
    String myQueueURL = sqs.createQueue(createQueueRequest).getQueueUrl();
    System.out.println("Sending msg '" + msg + "' to Q: " + myQueueURL);
    SendMessageRequest request = new SendMessageRequest()
        .withQueueUrl(myQueueURL)
        .withMessageGroupId(Constants.GROUP_ID)
        .withMessageBody(msg);
    SendMessageResult smr = sqs.sendMessage(request);

    return "SendMessage succeeded with messageId " + smr.getMessageId()
        + ", sequence number " + smr.getSequenceNumber() + "\n";
  }
}
