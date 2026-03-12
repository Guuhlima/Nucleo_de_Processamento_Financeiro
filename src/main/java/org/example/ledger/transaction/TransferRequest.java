package org.example.ledger.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequest(
        @NotNull UUID fromAccount,
        @NotNull UUID toAccount,
        @NotNull @DecimalMin(value = "0.0001") BigDecimal amount,
        @NotNull String idempotencyKey
) {
}
