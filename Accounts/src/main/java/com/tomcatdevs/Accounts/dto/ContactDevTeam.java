package com.tomcatdevs.Accounts.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;

@ConfigurationProperties(prefix = "accounts")
public record ContactDevTeam(String message, HashMap<String,String> contactDetails, List<String> onCallSupport) {
}
