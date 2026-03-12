package org.example.ledger.entry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, UUID> {

    List<LedgerEntry> findByAccountId(UUID accountId);

    @Query("""
        select coalesce(sum(
            case
                when e.direction = org.example.ledger.entry.EntryDirection.CREDIT then e.amount
                else -e.amount
            end
        ), 0)
        from LedgerEntry e
        where e.accountId = :accountId
        """)
    BigDecimal calculateBalance(UUID accountId);
}