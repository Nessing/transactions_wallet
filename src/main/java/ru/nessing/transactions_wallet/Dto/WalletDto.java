package ru.nessing.transactions_wallet.Dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class WalletDto {

    @NotNull(message = "Не указан ID кошелька")
    private UUID id;

    @NotBlank(message = "Не указан тип операции")
    @Pattern(regexp = "DEPOSIT|WITHDRAW", message = "Не корректно указана операция ('DEPOSIT' или 'WITHDRAW')")
    @NotNull
    private String operation;

    @DecimalMin(value = "0.1", message = "Сумма должна быть больше 0.1")
    @NotNull
    private Double amount;

    public UUID getId() {
        return id;
    }

    public String getOperation() {
        return operation;
    }

    public Double getAmount() {
        return amount;
    }
}
