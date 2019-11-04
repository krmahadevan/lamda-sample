package com.rationaleemotions.lambdas;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.rationaleemotions.Constants;

public class CredsPrinterLambda implements RequestHandler<String, String> {

  @Override
  public String handleRequest(String input, Context context) {
    String suffix = "[Access key = " + Constants.ACCESS_KEY + ", Secret key = " + Constants.SECRET_KEY
        + "]";
    return "Hello there " + input + ", your credentials " + suffix;
  }
}
