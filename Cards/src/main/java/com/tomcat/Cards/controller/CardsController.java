package com.tomcat.Cards.controller;

import com.tomcat.Cards.dto.CardsDto;
import com.tomcat.Cards.dto.ContactInfoCardsDevTeam;
import com.tomcat.Cards.dto.ErrorResponseDto;
import com.tomcat.Cards.dto.ResponseDto;
import com.tomcat.Cards.model.Cards;
import com.tomcat.Cards.service.iCardsServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.smartcardio.CardNotPresentException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/cards",produces = MediaType.APPLICATION_JSON_VALUE)
//@AllArgsConstructor
@Tag(
        name = "Cards Microservices",
        description = "EazyBank cards microservices restful API documentation"
)
@Validated
@EnableConfigurationProperties(value = {ContactInfoCardsDevTeam.class})
public class CardsController {

    private static final Logger logger = LoggerFactory.getLogger(CardsController.class);

    private final iCardsServices cardsServices;

    @Autowired
    private ContactInfoCardsDevTeam contactDevTeamInfo;

    public CardsController(iCardsServices cardsServices) {
        this.cardsServices = cardsServices;
    }

    @Value("${build.version}")
    private String buildVersion;

    @Operation(
            summary = "EazyBank issue new card to the customer"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "CREATED"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "INTERNAL_SERVER_ERROR"
                    ),
                    @ApiResponse(
                            responseCode = "417",
                            description = "EXPECTATION_FAILED",
                            content = @Content(
                                    contentSchema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    )
            }
    )
    @PostMapping(value = "/issueCard")
    public ResponseEntity<ResponseDto> issueNewCard(
                                                        @RequestParam
                                                        @Pattern(regexp = "$|[0-9]{10}",message = "mobile number must be of 10 digit only")
                                                        String mobileNumber) throws CardNotPresentException {
        boolean isCardIssued = cardsServices.createNewCard(mobileNumber);
        if(isCardIssued){
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDto(HttpStatus.ACCEPTED,
                            "woohooo!!, your credit card has been issued"));
        }else{
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(
                    HttpStatus.EXPECTATION_FAILED,"card not processed due to technical error"
            ));
        }
    }

    @Operation(
            summary = "EazyBank fetched card detail of the customer"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "CREATED"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "INTERNAL_SERVER_ERROR"
                    ),
                    @ApiResponse(
                            responseCode = "417",
                            description = "EXPECTATION_FAILED",
                            content = @Content(
                                    contentSchema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    )
            }
    )
    @GetMapping(value = "/fetchCard")
    public ResponseEntity<CardsDto> fetchCardCardNumber(
                                                  @Pattern(regexp = "$|[0-9]{10}",message = "card number must be of 10 digit only")
                                                  @RequestParam String cardNumber){
        CardsDto cardsDto = cardsServices.fetchCardDetailsByCardNumber(cardNumber);
        if(cardsDto != null){
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(cardsDto);
        }else{
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
        }
    }

    @GetMapping(value = "/fetch-card-by-mobileNumber")
    public ResponseEntity<CardsDto> fetchCardByMobileNumber(
            @RequestHeader("eazybank-correlation-id") String correlationId,
                                                     @Pattern(regexp = "$|[0-9]{10}",message = "mobile number must be of 10 digit only")
                                                     @RequestParam String mobileNumber){
//        System.out.println(mobileNumber);
        logger.debug("eazyBank-correlation-id found: {} ", correlationId);
        Optional<CardsDto> cardsOptional = cardsServices.fetchCardDetailsByMobileNumber(mobileNumber);
//        System.out.println(cardsOptional);
        return cardsOptional.map(cards -> ResponseEntity
                .status(HttpStatus.OK)
                .body(cards)).orElseGet(() -> ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(null));
    }

    /**
     * @Param- mobileNumber used to fetch card on which we want to update details
     *         like upgrade credit limit,extend expiration date, change card type,
     *         made card status active or freeze
     */
    @Operation(
            summary = "EazyBank update existing card details of the customer"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "CREATED"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "INTERNAL_SERVER_ERROR"
                    ),
                    @ApiResponse(
                            responseCode = "417",
                            description = "EXPECTATION_FAILED",
                            content = @Content(
                                    contentSchema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    )
            }
    )
    @PutMapping(value = "/updateCard")
    public ResponseEntity<ResponseDto> updateCard(@Valid @RequestBody CardsDto cardsDto){
        boolean isCardUpdated=cardsServices.cardDetailsUpdation(cardsDto);
        if(isCardUpdated){
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new ResponseDto(HttpStatus.ACCEPTED,"your card details updated successfully"));
        }else{
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(
                    HttpStatus.EXPECTATION_FAILED,"your card is not processed due to technical error,please try again and contact to your bank or website development team"
            ));
        }
    }

    @Operation(
            summary = "EazyBank process card closure the customer"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "INTERNAL_SERVER_ERROR"
                    ),
                    @ApiResponse(
                            responseCode = "417",
                            description = "EXPECTATION_FAILED",
                            content = @Content(
                                    contentSchema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    )
            }
    )
    @DeleteMapping(value = "/cardClosure")
    public ResponseEntity<ResponseDto> cardClosureRequestProcess( @RequestParam
                                                                     @Pattern(regexp = "$|[0-9]{10}",message = "mobile number must be of 10 digit only")
                                                                     String mobileNumber){
        boolean isClosed=cardsServices.cardClosureHandler(mobileNumber);
        if (isClosed){
            return new ResponseEntity<>(new ResponseDto(
                    HttpStatus.OK,"your card is closed successfully, we're unhappy to see this our valued customer will not take our services anymore"
            ), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(
                    new ResponseDto(HttpStatus.EXPECTATION_FAILED,
                            "card closure request not processed due to technical error, please contact to your bank or website development team"),
                    HttpStatus.EXPECTATION_FAILED);
        }
    }

    @Operation(
            summary = "EazyBank issue new card to the customer"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "INTERNAL_SERVER_ERROR"
                    ),
                    @ApiResponse(
                            responseCode = "417",
                            description = "EXPECTATION_FAILED",
                            content = @Content(
                                    contentSchema = @Schema(implementation = ErrorResponseDto.class)
                            )
                    )
            }
    )
    @GetMapping(value = "/allCards")
    public List<Cards> fetchAllCards() {
        return cardsServices.fetchingAllCustomersCards();
    }

    @GetMapping(value = "/contact-info")
    public ResponseEntity<ContactInfoCardsDevTeam> contactCardDevTeam(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(contactDevTeamInfo);
    }

    @GetMapping(value = "/version")
    public String getBuildVersion(){
        return buildVersion;
    }
}
