package com.ias;

import com.ias.model.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;


@Repository
@Slf4j
public class DynamoReactiveUserAdapter {
    private final DynamoDbEnhancedAsyncClient enhancedAsyncClient;
    private final DynamoDbAsyncTable<UserEntity> userEntityDynamoDbAsyncTable;

    public DynamoReactiveUserAdapter(DynamoDbEnhancedAsyncClient enhancedAsyncClient) {
        this.enhancedAsyncClient = enhancedAsyncClient;
        this.userEntityDynamoDbAsyncTable = enhancedAsyncClient.table(UserEntity.TABLE_NAME, TableSchema.fromBean(UserEntity.class));
    }

    public Flux<UserEntity> getAll() {
        return Flux.from(userEntityDynamoDbAsyncTable.scan().items());
    }

    public Mono<UserEntity> save(UserEntity userEntity) {
        return Mono.deferContextual(context -> {
            String traceUuid = context.get("traceUUID");
            return Mono.fromFuture(() -> userEntityDynamoDbAsyncTable.putItem(userEntity))
                    .doOnSuccess(item -> log.debug("Successfully saved item with trace UUID: {}", traceUuid))
                    .thenReturn(userEntity);
        });
    }


}
