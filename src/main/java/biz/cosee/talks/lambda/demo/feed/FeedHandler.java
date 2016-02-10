package biz.cosee.talks.lambda.demo.feed;

import biz.cosee.talks.lambda.demo.AWS;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Collections.sort;
import static java.util.Comparator.comparing;

public class FeedHandler implements RequestHandler<FeedRequest, FeedResponse> {


    public FeedResponse handleRequest(FeedRequest feedRequest, Context context) {
        AmazonDynamoDBClient dynamodb = new AmazonDynamoDBClient(AWS.getCredentialChain());
        dynamodb.setRegion(Region.getRegion(Regions.EU_WEST_1));

        DynamoDBMapper mapper = new DynamoDBMapper(dynamodb, AWS.getCredentialChain());

        String since = Long.toString(feedRequest.since);

        List<Picture> pictureForResponse = getPicturesSinceTimestamp(mapper, since);

        FeedResponse feedResponse = new FeedResponse();
        feedResponse.entries = pictureForResponse;
        feedResponse.total = 0L;

        return feedResponse;
    }

    private List<Picture> getPicturesSinceTimestamp(DynamoDBMapper mapper, String since) {
        PaginatedScanList<Picture> results = mapper.scan(Picture.class,
                new DynamoDBScanExpression()
                        .withFilterExpression("ts > :since")
                        .withExpressionAttributeValues(of(":since", new AttributeValue().withN(since))));
        System.out.println(results.size());

        results.loadAllResults();

        List<Picture> pictureForResponse = new ArrayList(results);

        sort(pictureForResponse, comparing(Picture::getTs).reversed());
        return pictureForResponse;
    }


}
