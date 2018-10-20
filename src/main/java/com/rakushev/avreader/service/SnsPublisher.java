package com.rakushev.avreader.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

public class SnsPublisher implements Publisher {
    private static final String topicArn = "arn:aws:sns:us-east-1:985923250937:av-by";
    private AmazonSNS snsClient;

    public SnsPublisher() {
        snsClient = AmazonSNSClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.US_EAST_1).build();
    }

    @Override
    public void sendNotification(String msg) {
        //publish to an SNS topic
        PublishRequest publishRequest = new PublishRequest(topicArn, msg);
        PublishResult publishResult = snsClient.publish(publishRequest);
        //print MessageId of message published to SNS topic
        System.out.println("MessageId - " + publishResult.getMessageId());
    }
}
