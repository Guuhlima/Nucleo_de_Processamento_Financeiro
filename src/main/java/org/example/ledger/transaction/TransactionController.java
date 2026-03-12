package org.example.ledger.transaction;

import org.example.ledger.entry.LedgerEntry;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public Transaction transfer(@Valid @RequestBody TransferRequest request) {
        return transactionService.transfer(request);
    }

    @GetMapping("/accounts/{accountId}/balance")
    public Map<String, BigDecimal> getBalance(@PathVariable UUID accountId) {
        return Map.of("balance", transactionService.getBalance(accountId));
    }

    @GetMapping("/accounts/{accountId}/entries")
    public List<LedgerEntry> getEntries(@PathVariable UUID accountId) {
        return transactionService.getEntries(accountId);
    }
}
