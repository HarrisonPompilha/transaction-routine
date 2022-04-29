package br.com.pismo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.pismo.model.ResponseMessage;
import br.com.pismo.model.dto.TransactionForm;
import br.com.pismo.utils.ConstantMapping;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TransactionsMappingTests extends CommonTest {
    
    @Test
    void mustCreateAnTransaction() throws JsonProcessingException, Exception {
        createAccount("91074708008");
        MvcResult result = this.mockMvc.perform(post(ConstantMapping.TRANSACTIONS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new TransactionForm(1L, 1, new BigDecimal(500.0)))))
                .andExpect(status().isCreated()).andReturn();
        
        ResponseMessage responseMessage = mapper.readValue(result.getResponse().getContentAsString(), ResponseMessage.class);
        
        assertEquals(responseMessage.getMessage(), "OK");
        
    }

    @Test
    void mustReturnAccountDoesntExists() throws JsonProcessingException, Exception {
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

}
