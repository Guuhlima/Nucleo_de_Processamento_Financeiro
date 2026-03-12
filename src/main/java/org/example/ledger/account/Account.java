package org.example.ledger.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    private  UUID id;
    private  UUID ownerId;
    private String currency;
    private OffsetDateTime createdAt;

    public Account() {
    }

    public Account(UUID Id, UUID ownerId, String currency, OffsetDateTime createdAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.currency = currency;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public String getCurrency() {
        return currency;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
