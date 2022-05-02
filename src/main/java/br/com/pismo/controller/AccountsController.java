package br.com.pismo.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.pismo.model.Account;
import br.com.pismo.model.dto.AccountDto;
import br.com.pismo.model.dto.AccountForm;
import br.com.pismo.repository.AccountRepository;
import br.com.pismo.utils.ConstantMapping;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(path = ConstantMapping.ACCOUNTS, produces = APPLICATION_JSON_VALUE)
@Tag(name = "Accounts Controller")
public class AccountsController {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Operation(summary = "Creates an account")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Account Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "422", description = "Account already exists")
        }
    ) 
    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody @Valid AccountForm payload, UriComponentsBuilder uriComponentsBuilder){
        if (accountRepository.findByDocument(payload.getDocument()) == null) {
            Account account = new Account();
            BeanUtils.copyProperties(payload, account);
            Account saved = accountRepository.save(account);
            URI uri = uriComponentsBuilder.path(ConstantMapping.ACCOUNTS + "/{id}").buildAndExpand(saved.getId()).toUri();
            return ResponseEntity.created(uri).build();
        } else {
            return ResponseEntity.unprocessableEntity().build();
        }
    }
    
    @Operation(summary = "Get an account based on id")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "200", description = "Account Retrieved", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AccountDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "422", description = "Account already exists")
        }
    ) 
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable("accountId") Long id){
        Optional<Account> account = accountRepository.findById(id);
        
        if (account.isPresent()) {
            AccountDto accountDto = new AccountDto();
            BeanUtils.copyProperties(account.get(), accountDto);
            return new ResponseEntity<AccountDto>(accountDto, HttpStatus.OK);
        }
        
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
}
