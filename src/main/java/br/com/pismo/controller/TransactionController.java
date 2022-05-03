package br.com.pismo.controller;

import static br.com.pismo.utils.ConstantMapping.TRANSACTIONS;

import java.math.BigDecimal;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pismo.enums.OperationTypes;
import br.com.pismo.model.Account;
import br.com.pismo.model.AccountCreditLimit;
import br.com.pismo.model.OperationType;
import br.com.pismo.model.ResponseMessage;
import br.com.pismo.model.Transaction;
import br.com.pismo.model.dto.TransactionForm;
import br.com.pismo.repository.AccountCreditLimitRepository;
import br.com.pismo.repository.AccountRepository;
import br.com.pismo.repository.TransactionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping(path = TRANSACTIONS, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Transactions Controller")
public class TransactionController {
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AccountCreditLimitRepository accountLimitRepository;
    
    @Operation(summary = "Creates an transaction based on the account and the operation id")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Created transaction", 
        content = { @Content(mediaType = "application/json") }),
      @ApiResponse(responseCode = "400", description = "Account or Operation does not exists", 
              content = { @Content(mediaType = "application/json", 
              schema = @Schema(implementation = ResponseMessage.class)) })}) 
    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody @Valid TransactionForm form){
        
        Optional<Account> account = accountRepository.findById(form.getAccountId());
        
        if (!account.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ACCOUNT DOES NOT EXIST"));
        }
        
        Optional<OperationTypes> operationTypeEnum = OperationTypes.fromId(form.getOperationTypeId());
        
        if (operationTypeEnum.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("OPERATION TYPE DOES NOT EXIST"));
        }
                        
        OperationType operationType = OperationType.builder().withId(operationTypeEnum.get().getId()).build();
        BigDecimal transactionAmount = Transaction.calculateAmount(form.getAmount(), operationTypeEnum.get().getCalculation());
        
        AccountCreditLimit accountCreditLimit = accountLimitRepository.findById(form.getAccountId()).get();
        
        BigDecimal finalValue = accountCreditLimit.getAccountLimit().add(transactionAmount);
        
        if (finalValue.compareTo(new BigDecimal(0)) == -1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("TRANSACTION NOT ALLOWED"));
        }
        
        Transaction transaction = Transaction.builder()
                .withAccount(account.get())
                .withOperationType(operationType)
                .withAmount(transactionAmount) //Calcular o valor real para gravar na tabela
                .build();
        
        transactionRepository.save(transaction);
        
        accountCreditLimit.setAccountLimit(finalValue);
        accountLimitRepository.save(accountCreditLimit);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage("OK"));
    }

    
}
