package br.com.pismo;

import static br.com.pismo.utils.ConstantMapping.ACCOUNTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pismo.model.ResponseMessage;
import br.com.pismo.model.dto.AccountDto;
import br.com.pismo.model.dto.AccountForm;
import br.com.pismo.model.dto.TransactionForm;
import br.com.pismo.utils.ConstantMapping;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper mapper;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
	@Test
	public void mustCreateAnAccount() throws JsonProcessingException, Exception {
	    this.mockMvc.perform(post(ACCOUNTS)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(mapper.writeValueAsString(new AccountForm("98038538056"))))
	            .andExpect(status().isCreated())
	            .andExpect(header().exists(HttpHeaders.LOCATION));
	}

	@Test
	public void mustNotCreateAnAccount() throws JsonProcessingException, Exception {
	    this.mockMvc.perform(post(ACCOUNTS)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(mapper.writeValueAsString(new AccountForm("123"))))
	    .andExpect(status().isBadRequest());
	}
	
	
	@Test
	public void mustRetrieveAnAccount() throws JsonProcessingException, Exception {
	    String accountCreated = createAccount("29534593087");
	    
	    MvcResult result = this.mockMvc.perform(get(accountCreated))
	        .andExpect(status().isOk())
	        .andReturn();
	    
	    AccountDto accountDto = mapper.readValue(result.getResponse().getContentAsString(), AccountDto.class);
	    
	    assertEquals(accountDto.getDocument(), "29534593087");
	    
	}

    @Test
    public void mustNotRetrieveAnAccount() throws JsonProcessingException, Exception {
        this.mockMvc.perform(get(ACCOUNTS + "/500")).andExpect(status().isNotFound());
    }
    
    @Test
    public void mustCreateAnTransaction() throws JsonProcessingException, Exception {
        createAccount("91074708008");
        MvcResult result = this.mockMvc.perform(post(ConstantMapping.TRANSACTIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new TransactionForm(1L, 1, new BigDecimal(500.0)))))
                .andExpect(status().isCreated()).andReturn();
        
        ResponseMessage responseMessage = mapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);
        
        assertEquals(responseMessage.getMessage(), "OK");
        
    }

    @Test
    public void mustReturnAccountDoesntExists() throws JsonProcessingException, Exception {
        MvcResult result = this.mockMvc.perform(post(ConstantMapping.TRANSACTIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new TransactionForm(90L, 1, new BigDecimal(500.0)))))
                .andExpect(status().isNotFound()).andReturn();
        
        ResponseMessage responseMessage = mapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);
        
        assertEquals(responseMessage.getMessage(), "ACCOUNT DOES NOT EXIST");
        
    }
    
    @Test
    public void mustReturnOperationIdDoesntExists() throws JsonProcessingException, Exception {
        MvcResult result = this.mockMvc.perform(post(ConstantMapping.TRANSACTIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new TransactionForm(1L, 9, new BigDecimal(500.0)))))
                .andExpect(status().isNotFound()).andReturn();
        
        ResponseMessage responseMessage = mapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);
        
        assertEquals(responseMessage.getMessage(), "OPERATION TYPE DOES NOT EXIST");
        
    }

    private String createAccount(String document) {
        return restTemplate.postForLocation(ACCOUNTS, new AccountForm(document)).getPath(); 
    }

}
