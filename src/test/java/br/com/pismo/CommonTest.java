package br.com.pismo;

import static br.com.pismo.utils.ConstantMapping.ACCOUNTS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pismo.model.dto.AccountForm;

public class CommonTest {
    
    @Autowired
    protected MockMvc mockMvc;
    
    @Autowired
    protected ObjectMapper mapper;
    
    @Autowired
    protected TestRestTemplate restTemplate;
    
    protected String createAccount(String document) {
        return restTemplate.postForLocation(ACCOUNTS, new AccountForm(document)).getPath(); 
    }
}
