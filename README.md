# AWS Lambda 

## Setup

### Install the SDK tool kit:

Run the below commands:

```
pip3 install --upgrade --user awscli
```

More instructions are available [here](https://docs.aws.amazon.com/cli/latest/userguide/cli-chap-install.html)

### Install the IntelliJ AWS Toolkit plugin:

Refer [here](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/welcome.html) for installing the AWS Toolkit plugin for IntelliJ.

### Install Serverless Model Command Line Interface (SAML)

Refer [here](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html) for installation instructions.


## Documentation

Complete documentation for Lambdas is available [here](https://docs.aws.amazon.com/lambda/latest/dg/welcome.html)

## Invoking the Lamda

### From command line 

To invoke a lambda from the command line refer [here](https://docs.aws.amazon.com/lambda/latest/dg/gettingstarted-awscli.html)

### From IntelliJ

To run a lambda from within IntelliJ refer [here](https://docs.aws.amazon.com/toolkit-for-jetbrains/latest/userguide/ui-reference.html)

## Quick Reference commands

### Create a bucket

```
aws s3 mb s3://re.lambdas.jars
```
### Build the code

```
mvn clean package
```
### Create and deploy the lambda function

```
aws lambda create-function \
--function-name creds-printer \
--zip-file fileb://target/lambda-1.0-SNAPSHOT.jar \
--handler com.rationaleemotions.lambdas.CredsPrinterLambda::handleRequest \
--runtime java8 \
--timeout 900 \
--role arn:aws:iam::<UserAccountGoesHere>:role/lambda-cli-role
```

Here **lambda-cli-role** represents a role that has permissions

## Invoke the function

```
aws lambda invoke --invocation-type RequestResponse \
--function-name creds-printer \
--payload \"world\" outputfile.txt
```
## Invoking the lambda via the Trigger

```
curl -X GET \
  <URL> \
  -H 'Accept: */*' \
  -H 'Accept-Encoding: gzip, deflate' \
  -H 'Cache-Control: no-cache' \
  -H 'Connection: keep-alive' \
  -H 'Host: s63gqp2gf7.execute-api.ap-south-1.amazonaws.com' \
  -H 'cache-control: no-cache' \
  -H 'x-api-key: <APIKeyGoesHere>'
```

### Miscellaneous references

Refer [here](http://doloadtest.blogspot.com/2018/04/upload-download-from-aws-s3-via-jmeter.html) to learn how to interact with AWS from within JMeter.