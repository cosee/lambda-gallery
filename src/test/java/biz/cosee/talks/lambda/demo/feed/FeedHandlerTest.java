package biz.cosee.talks.lambda.demo.feed;

import org.junit.Ignore;
import org.junit.Test;

public class FeedHandlerTest {

    @Test
    @Ignore
    public void test() {
        FeedRequest feedRequest = new FeedRequest();
        feedRequest.since = 1448286875725L;
        FeedResponse feedResponse = new FeedHandler().handleRequest(feedRequest, null);

        System.out.println(feedResponse);
    }

}