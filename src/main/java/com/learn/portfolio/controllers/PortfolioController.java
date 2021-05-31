package com.learn.portfolio.controllers;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.learn.portfolio.model.GetHoldingsResponse;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

@Controller
public class PortfolioController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private EurekaClient eurekaClient;
	
	 @RequestMapping(value = "/portfolio")
	    public String viewPortfolio() {
		 
		 GetHoldingsResponse response = getHoldingsForUser(getRemoteUser());
		 System.out.println(response);
		 
	        return "portfolio";
	    }
	 
	 private GetHoldingsResponse getHoldingsForUser(String userId)
	 {
		 InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("portfolio-service", false);
		 String baseUrl = instanceInfo.getHomePageUrl();
		 HttpHeaders headers = new HttpHeaders();
	      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	      HttpEntity <String> entity = new HttpEntity<String>(headers);
	      return restTemplate.exchange(baseUrl + "/portfolio-service/services/holdings/getallforuser/" + userId, HttpMethod.GET, entity, GetHoldingsResponse.class).getBody();
	 }
	 
	 private String getRemoteUser()
	 {
		 String username = null;
		 Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		 if (principal instanceof UserDetails) {
		   username = ((UserDetails)principal).getUsername();
		 } else {
		   username = principal.toString();
		 }
		 return username;
	 }

}
