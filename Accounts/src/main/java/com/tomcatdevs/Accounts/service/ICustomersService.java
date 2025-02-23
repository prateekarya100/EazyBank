package com.tomcatdevs.Accounts.service;


import com.tomcatdevs.Accounts.dto.CustomerDetailsDto;

public interface ICustomersService {

    CustomerDetailsDto fetchConsolidatedCustomerDetails(String mobileNumber);
}
