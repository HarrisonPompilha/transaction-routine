package br.com.pismo.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pismo.model.AccountCreditLimit;
import br.com.pismo.model.dto.AccountLimitDto;
import br.com.pismo.repository.AccountCreditLimitRepository;
import br.com.pismo.utils.ConstantMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = ConstantMapping.ACCOUNT_LIMIT, produces = APPLICATION_JSON_VALUE)
@Tag(name = "Accounts Controller")
public class AccountLimitController {
    
    @Autowired
    private AccountCreditLimitRepository accountCreditLimitRepository;
    
    
    @Operation(summary = "Get the limit of an account based on id")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Account Retrieved", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountLimitDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request")
        }
    ) 
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountLimitDto> getAccountById(@PathVariable("accountId") Long id){
        Optional<AccountCreditLimit> account = accountCreditLimitRepository.findById(id);
        
        if (account.isPresent()) {
            AccountLimitDto accountLimit = new AccountLimitDto();
            BeanUtils.copyProperties(account.get(), accountLimit);
            return new ResponseEntity<AccountLimitDto>(accountLimit, HttpStatus.OK);
        }
        
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
}
