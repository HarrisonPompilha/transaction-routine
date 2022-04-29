package br.com.pismo;

import static br.com.pismo.utils.ConstantMapping.ACCOUNTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.pismo.model.dto.AccountDto;
import br.com.pismo.model.dto.AccountForm;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AccountMappingTests extends CommonTest {
    
	@Test
	void mustCreateAnAccount() throws JsonProcessingException, Exception {
	    this.mockMvc.perform(post(ACCOUNTS)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(mapper.writeValueAsString(new AccountForm("98038538056"))))
	            .andExpect(status().isCreated())
	            .andExpect(header().exists(HttpHeaders.LOCATION));
	}

	@Test
	void mustNotCreateAnAccount() throws JsonProcessingException, Exception {
	    this.mockMvc.perform(post(ACCOUNTS)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(mapper.writeValueAsString(new AccountForm("123"))))
	    .andExpect(status().isBadRequest());
	}
	
	
	@Test
	void mustRetrieveAnAccount() throws JsonProcessingException, Exception {
	    String accountCreated = createAccount("29534593087");
	    
	    MvcResult result = this.mockMvc.perform(get(accountCreated))
	        .andExpect(status().isOk())
	        .andReturn();
	    
	    AccountDto accountDto = mapper.readValue(result.getResponse().getContentAsString(), AccountDto.class);
	    
	    assertEquals("29534593087", accountDto.getDocument());
	    
	}

    @Test
    void mustNotRetrieveAnAccount() throws JsonProcessingException, Exception {
        this.mockMvc.perform(get(ACCOUNTS + "/500")).andExpect(status().isNotFound());
    }
    
}
