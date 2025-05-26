package com.tomcatdevs.Accounts.service.clients;

import com.tomcatdevs.Accounts.dto.LoansDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("loans")
public interface LoansFeignClient {

    @GetMapping(value = "/api/loans/fetchLoan",consumes = "application/json")
    public ResponseEntity<LoansDto> fetchLoans(@RequestParam String mobileNumber);

}
