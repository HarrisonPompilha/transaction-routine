package br.com.pismo.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class Transaction {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;
    
    @OneToOne
    @JoinColumn(name = "operation_type_id")
    private OperationType operationType;
    
    private BigDecimal amount;
    
    private final LocalDateTime eventDate = LocalDateTime.now(ZoneId.of("UTC"));
    
    public static BigDecimal calculateAmount(BigDecimal amount, int value) {
        return amount.multiply(new BigDecimal(value));
    }
    
}
