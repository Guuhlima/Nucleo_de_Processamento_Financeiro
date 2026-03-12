package org.example.ledger.account;

import org.example.ledger.common.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account create(CreateAccountRequest request) {
        Account account = new Account(
                UUID.randomUUID(),
                request.ownerId(),
                request.currency().toUpperCase(),
                OffsetDateTime.now()
        );

        return accountRepository.save(account);
    }

    public Account findById(UUID id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conta não encontrada: " + id));
    }

    public List<Account> findAll() {
        return accountRepository.findAll();
    }
}
