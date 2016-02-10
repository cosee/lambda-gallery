package biz.cosee.talks.lambda.demo.s3;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;

public class UploadHandlerTest {

    @Test
    @Ignore
    public void testHandleRequest() throws Exception {
        UploadHandler uploadHandler = new UploadHandler();

        S3EventNotification.S3BucketEntity bucketEntity = new S3EventNotification.S3BucketEntity("mohrhard-devopscon", null, "");
        S3EventNotification.S3ObjectEntity objectEntity = new S3EventNotification.S3ObjectEntity("test2.jpg", 0L, "", "");
        S3EventNotification.S3Entity s3Entity = new S3EventNotification.S3Entity("", bucketEntity, objectEntity, null);
        Context context = null;
        uploadHandler.handleRequest(new S3Event(Collections.singletonList(
                new S3EventNotification.S3EventNotificationRecord("eu-west-1", null, null, null, null, null, null, s3Entity, null))), context);
    }
}