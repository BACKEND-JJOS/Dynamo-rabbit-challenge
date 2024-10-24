package com.ias.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Getter
@Setter
@ToString
@DynamoDbBean
public class UserEntity {
    public static final String TABLE_NAME = "user";
    private String id;
    private String name;
    private String status;

    @DynamoDbPartitionKey
    public String getId() {
        return this.id;
    }
}
