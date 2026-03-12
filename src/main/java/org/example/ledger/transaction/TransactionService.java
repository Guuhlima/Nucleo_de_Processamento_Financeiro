package org.example.ledger.transaction;

import org.example.ledger.account.Account;
import org.example.ledger.account.AccountService;
import org.example.ledger.common.BusinessException;
import org.example.ledger.entry.EntryDirection;
import org.example.ledger.entry.LedgerEntry;
import org.example.ledger.entry.LedgerEntryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository,
                              LedgerEntryRepository ledgerEntryRepository,
                              AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.ledgerEntryRepository = ledgerEntryRepository;
        this.accountService = accountService;
    }

    @Transactional
    public Transaction transfer(TransferRequest request) {
        if (request.fromAccount().equals(request.toAccount())) {
            throw new BusinessException("Conta de origem e destino não podem ser iguais");
        }

        transactionRepository.findByIdempotencyKey(request.idempotencyKey())
                .ifPresent(existing -> {
                    throw new BusinessException("Idempotency key já utilizada");
                });

        Account source = accountService.findById(request.fromAccount());
        Account destination = accountService.findById(request.toAccount());

        validateCurrency(source, destination);
        validateAmount(request.amount());

        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                request.idempotencyKey(),
                "TRANSFER",
                OffsetDateTime.now()
        );

        transactionRepository.save(transaction);

        LedgerEntry debitEntry = new LedgerEntry(
                UUID.randomUUID(),
                transaction.getId(),
                source.getId(),
                request.amount(),
                EntryDirection.DEBIT,
                OffsetDateTime.now()
        );

        LedgerEntry creditEntry = new LedgerEntry(
                UUID.randomUUID(),
                transaction.getId(),
                source.getId(),
                request.amount(),
                EntryDirection.CREDIT,
                OffsetDateTime.now()
        );

        validateDoubleEntry(List.of(debitEntry), List.of(creditEntry));

        ledgerEntryRepository.save(debitEntry);
        ledgerEntryRepository.save(creditEntry);

        return transaction;
    }

    public BigDecimal getBalance(UUID accountId) {
        accountService.findById(accountId);
        return ledgerEntryRepository.calculateBalance(accountId);
    }

    public List<LedgerEntry> getEntries(UUID accountId) {
        accountService.findById(accountId);
        return ledgerEntryRepository.findByAccountId(accountId);
    }

    private void validateCurrency(Account source, Account destination) {
        if (!source.getCurrency().equalsIgnoreCase(destination.getCurrency())) {
            throw new BusinessException("Transferência entre moedas diferentes não é permitida");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new BusinessException("Valor da transação deve ser maior que zero");
        }
    }

    private void validateDoubleEntry(List<LedgerEntry> debits, List<LedgerEntry> credits) {
        BigDecimal totalDebits = debits.stream()
                .map(LedgerEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCredits = credits.stream()
                .map(LedgerEntry::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalDebits.compareTo(totalCredits) != 0) {
            throw new BusinessException("Transação inválida: débitos e créditos não se equilibram");
        }
    }
}
