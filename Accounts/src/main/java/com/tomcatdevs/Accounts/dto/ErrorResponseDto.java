package com.tomcatdevs.Accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
@Schema(
        name = "ErrorResponse",
        description = "schema to hold rest api error responses"
)
public class ErrorResponseDto {
    @Schema(
            description = "path due to which error occured"
    )
    private String apiPath;

    @Schema(
            description = "error code generated from server"
    )
    private HttpStatus errorCode;

    @Schema(
            description = "response generated by server"
    )
    private String serverResponse;

    @Schema(
            description = "time when error occur"
    )
    private LocalDateTime errorTime;
}
