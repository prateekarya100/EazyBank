package com.tomcatdevs.Accounts.service.Impl;

import com.tomcatdevs.Accounts.dto.AccountsDto;
import com.tomcatdevs.Accounts.dto.CardsDto;
import com.tomcatdevs.Accounts.dto.CustomerDetailsDto;
import com.tomcatdevs.Accounts.dto.LoansDto;
import com.tomcatdevs.Accounts.exception.ResourceNotFoundException;
import com.tomcatdevs.Accounts.mapper.AccountsMapper;
import com.tomcatdevs.Accounts.mapper.CustomerMapper;
import com.tomcatdevs.Accounts.model.Accounts;
import com.tomcatdevs.Accounts.model.Customer;
import com.tomcatdevs.Accounts.repository.AccountsRepository;
import com.tomcatdevs.Accounts.repository.CustomerRepository;
import com.tomcatdevs.Accounts.service.ICustomersService;
import com.tomcatdevs.Accounts.service.clients.CardsFeignClient;
import com.tomcatdevs.Accounts.service.clients.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ICustomersServiceImpl implements ICustomersService {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private CardsFeignClient cardsFeignClient;

    @Autowired
    private LoansFeignClient loansFeignClient;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public CustomerDetailsDto fetchConsolidatedCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()->new ResourceNotFoundException("customer", "mobile number", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                ()->new ResourceNotFoundException("customer", "mobile number", mobileNumber)
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer,new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapAccountsToDto(accounts,new AccountsDto()));

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardByMobileNumber(mobileNumber);
        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoans(mobileNumber);

        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());

        return customerDetailsDto;
    }
}
