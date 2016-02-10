package biz.cosee.talks.lambda.demo.s3;

import biz.cosee.talks.lambda.demo.AWS;
import biz.cosee.talks.lambda.demo.feed.Picture;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.UUID;

public class UploadHandler implements RequestHandler<S3Event, Void> {

    public Void handleRequest(S3Event s3Event, Context context) {
        try {
            S3EventNotification.S3Entity s3Entity = s3Event.getRecords().get(0).getS3();

            String bucket = s3Entity.getBucket().getName();
            String key = s3Entity.getObject().getKey();

            File image = downloadFileFromS3(bucket, key);

            Metadata metadata = ImageMetadataReader.readMetadata(image);

            writeMetadataToDynamoDb(bucket, key, metadata);

            image.delete();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    private File downloadFileFromS3(String bucket, String key) throws IOException, InterruptedException {
        AmazonS3Client amazonS3Client = new AmazonS3Client(AWS.getCredentialChain());
        TransferManager transferManager = new TransferManager(amazonS3Client);

        Path tempFile = Files.createTempFile("lambda-gallery", ".jpg");
        Download download = transferManager.download(bucket, key, tempFile.toFile());

        download.waitForCompletion();

        return tempFile.toFile();
    }

    private void writeMetadataToDynamoDb(String bucket, String key, Metadata metadata) {
        final Collection<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);

        GeoLocation geoLocation = gpsDirectories.iterator().next().getGeoLocation();


        AmazonDynamoDBClient amazonDynamoDBClient = new AmazonDynamoDBClient(AWS.getCredentialChain());
        amazonDynamoDBClient.setRegion(Region.getRegion(Regions.EU_WEST_1));

        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDBClient, AWS.getCredentialChain());
        Picture picture = new Picture();
        picture.setId(UUID.randomUUID().toString());

        picture.setTs(System.currentTimeMillis());

        picture.setBucket(bucket);
        picture.setKey(key);

        picture.setLat(geoLocation.getLatitude());
        picture.setLon(geoLocation.getLongitude());

        dynamoDBMapper.save(picture);
    }


}
