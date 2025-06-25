package com.tomcatdevs.Accounts.controller;

import com.tomcatdevs.Accounts.dto.CustomerDetailsDto;
import com.tomcatdevs.Accounts.dto.ErrorResponseDto;
import com.tomcatdevs.Accounts.service.ICustomersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/consolidated",produces = MediaType.APPLICATION_JSON_VALUE)
//@AllArgsConstructor
@Validated
@Tag(
        name = "EazyBank CustomerController",
        description = "EazyBank CustomerDetails restful services documentation"
)
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final ICustomersService iCustomersService;

    public CustomerController(ICustomersService iCustomersService) {
        this.iCustomersService = iCustomersService;
    }


    @Operation(
            summary = "fetch bank customers all account related details from eazybank",
            description = "EazyBank accounts,cards and loans microservices restful services documentation"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description="Request Accepted"
                    ),
                    @ApiResponse(
                            responseCode = "417",
                            description = "Expectation Failed"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description="Internal_Server_Error",
                            content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
                    )
            }
    )
    @GetMapping("/CustomerDetails")
    public ResponseEntity<CustomerDetailsDto> fetchConsolidatedCustomerDetails(
                                                                               @RequestHeader("eazybank-correlation-id") String correlationId,
                                                                               @RequestParam
                                                                               @Pattern(regexp = "$|[0-9]{10}",message = "number must be of 10 digit")
                                                                               String mobileNumber){
        logger.debug("eazybank-correlation-id found : {} ",correlationId);
        CustomerDetailsDto customerDetailsDto = iCustomersService.fetchConsolidatedCustomerDetails(mobileNumber,correlationId);
        return ResponseEntity.status(HttpStatus.OK)
                .header("eazybank-correlation-id", correlationId)
                .body(customerDetailsDto);
    }
}
