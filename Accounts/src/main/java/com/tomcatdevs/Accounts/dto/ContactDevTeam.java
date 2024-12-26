package com.tomcatdevs.Accounts.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;

@ConfigurationProperties(prefix = "accounts")
@Getter @Setter
public class ContactDevTeam{
    private String message;
    private HashMap<String,String> contactDetails;
    private List<String> onCallSupport;
}
