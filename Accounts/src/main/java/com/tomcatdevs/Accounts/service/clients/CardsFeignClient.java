package com.tomcatdevs.Accounts.service.clients;

import com.tomcatdevs.Accounts.dto.CardsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("cards")
public interface CardsFeignClient {

    @GetMapping(value = "/api/cards/fetch-card-by-mobileNumber",consumes = "application/json")
    public ResponseEntity<CardsDto> fetchCardByMobileNumber(@RequestParam String mobileNumber);
}
