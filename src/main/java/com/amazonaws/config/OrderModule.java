/*
 * Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.amazonaws.config;

import com.amazonaws.dao.OrderDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

import javax.inject.Named;
import java.net.URI;
import java.util.Optional;


public class OrderModule {


    private static OrderModule INSTANCE = new OrderModule();
    private String tableName;
    private DynamoDbClient dynamoDbClient;
    private ObjectMapper objectMapper;
    private OrderDao orderDao;

    private OrderModule() {
        tableName = tableName();
        dynamoDbClient = dynamoDb();
        objectMapper = objectMapper();
        orderDao = orderDao(dynamoDbClient, tableName);

    }


    public static OrderModule getInstance() {
        return INSTANCE;
    }


    private String tableName() {
        return Optional.ofNullable(System.getenv("TABLE_NAME")).orElse("orders_table");
    }


    private DynamoDbClient dynamoDb() {
        final String endpoint = System.getenv("ENDPOINT_OVERRIDE");

        DynamoDbClientBuilder builder = DynamoDbClient.builder();
        builder.httpClient(ApacheHttpClient.builder().build());
        if (endpoint != null && !endpoint.isEmpty()) {
            builder.endpointOverride(URI.create(endpoint));
        }

        return builder.build();
    }


    private ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    private OrderDao orderDao(DynamoDbClient dynamoDb, @Named("tableName") String tableName) {
        return new OrderDao(dynamoDb, tableName, 10);
    }

    public String getTableName() {
        return tableName;
    }

    public DynamoDbClient getDynamoDbClient() {
        return dynamoDbClient;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }
}
