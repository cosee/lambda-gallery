package biz.cosee.talks.lambda.demo;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

public class AWS {
    public static AWSCredentialsProvider getCredentialChain() {
        return new AWSCredentialsProviderChain(new EnvironmentVariableCredentialsProvider(), new ProfileCredentialsProvider("devopscon"));
    }
}
