package br.com.pismo.model.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAlias;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "Transaction")
@AllArgsConstructor
@NoArgsConstructor
public class TransactionForm {
    
    @NotNull
    @JsonAlias("account_id")
    @Schema(description = "User Account ID", required = true)
    private Long accountId;
    
    @NotNull
    @JsonAlias("operation_type_id")
    @Schema(description = "Operation Type ID",
        example = "1",
        required = true,
        allowableValues = {"1", "2", "3", "4"})
    private int operationTypeId;
    
    @NotNull
    @Schema(description = "Transaction Value", 
        example = "500.00", 
        required = true,
        format = "type: BigDecimal")
    private BigDecimal amount;
    
}
