package org.example.ledger.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateAccountRequest(
    @NotNull UUID ownerId,
    @NotBlank String currency
) {
}