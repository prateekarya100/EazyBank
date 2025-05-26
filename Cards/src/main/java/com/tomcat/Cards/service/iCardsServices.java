package com.tomcat.Cards.service;

import com.tomcat.Cards.dto.CardsDto;
import com.tomcat.Cards.model.Cards;
import jakarta.validation.constraints.Pattern;

import javax.smartcardio.CardNotPresentException;
import java.util.List;
import java.util.Optional;

public interface iCardsServices {
    boolean createNewCard(String mobileNumber) throws CardNotPresentException;

    List<Cards> fetchingAllCustomersCards();

    CardsDto fetchCardDetailsByCardNumber(String cardNumber);

    boolean cardDetailsUpdation(CardsDto cardsDto);

    boolean cardClosureHandler(String mobileNumber);

    Optional<CardsDto> fetchCardDetailsByMobileNumber(@Pattern(regexp = "$|[0-9]{10}",message = "mobile number must be of 10 digit only") String mobile);
}
