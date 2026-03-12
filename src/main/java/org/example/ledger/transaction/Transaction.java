package org.example.ledger.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String idempotencyKey;

    private String type;
    private OffsetDateTime createdAt;

    public Transaction() {
    }

    public Transaction(UUID id, String idempotencyKey, String type, OffsetDateTime createdAt) {
        this.id = id;
        this.idempotencyKey = idempotencyKey;
        this.type = type;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getType() {
        return type;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}