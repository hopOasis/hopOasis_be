package com.example.hop_oasis;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.model.HttpApiV2ProxyRequest;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StreamLambdaHandler implements RequestStreamHandler {

    private static final SpringBootLambdaContainerHandler<HttpApiV2ProxyRequest, AwsProxyResponse> LAMBDA_CONTAINER_HANDLER;

    static {
        try {
            LAMBDA_CONTAINER_HANDLER = SpringBootLambdaContainerHandler.getHttpApiV2ProxyHandler(HopOasisApplication.class);
        } catch (ContainerInitializationException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not initialize Spring Boot application", e);
        }
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        log.info("Handling request");
        LAMBDA_CONTAINER_HANDLER.proxyStream(inputStream, outputStream, context);
    }

}
