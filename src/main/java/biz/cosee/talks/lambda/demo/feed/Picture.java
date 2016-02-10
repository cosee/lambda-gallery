package biz.cosee.talks.lambda.demo.feed;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

@DynamoDBTable(tableName = "tmp-techtalk")
public class Picture {

    @DynamoDBHashKey(attributeName = "id")
    private String id;
    @DynamoDBRangeKey(attributeName = "ts")
    private long ts;

    @DynamoDBAttribute(attributeName = "lat")
    private double lat;
    @DynamoDBAttribute(attributeName = "lon")
    private double lon;

    @DynamoDBAttribute(attributeName = "bucket")
    private String bucket;

    @DynamoDBAttribute(attributeName = "key")
    private String key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
