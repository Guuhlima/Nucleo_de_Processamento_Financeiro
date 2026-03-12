package org.example.ledger.entry;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ledger_entries")
public class LedgerEntry {
    @Id
    private UUID id;

    private UUID transactionId;
    private UUID accountId;

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private EntryDirection direction;

    private OffsetDateTime createdAt;

    public LedgerEntry() {
    }

    public LedgerEntry(UUID id, UUID transactionId, UUID accountId, BigDecimal amount,
                       EntryDirection direction, OffsetDateTime createdAt) {
        this.id = id;
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.amount = amount;
        this.direction = direction;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public EntryDirection getDirection() {
        return direction;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
