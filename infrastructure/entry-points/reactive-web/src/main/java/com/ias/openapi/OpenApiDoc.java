package com.ias.openapi;

import com.ias.response.UserResponse;
import lombok.experimental.UtilityClass;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springframework.http.HttpStatus;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.arrayschema.Builder.arraySchemaBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

@UtilityClass
public class OpenApiDoc {

    private static final String MEDIA_TYPE_APPLICATION_JSON = "application/json";

    private static final String TAG_USER = "User";

    public Builder getUpdatedUsers(Builder builder) {
        return builder.operationId("getUpdatedUsers")
                .description("Get the list of updated users")
                .response(
                        responseBuilder()
                                .responseCode(HttpStatus.OK.toString())
                                .description("List of updated users")
                                .content(
                                        contentBuilder()
                                                .mediaType(MEDIA_TYPE_APPLICATION_JSON)
                                                .array(
                                                        arraySchemaBuilder().schema(
                                                                schemaBuilder().implementation(UserResponse.class)
                                                        )
                                                )
                                )
                )
                .tag(TAG_USER);
    }
}
