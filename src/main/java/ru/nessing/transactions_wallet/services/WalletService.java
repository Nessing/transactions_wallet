package ru.nessing.transactions_wallet.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import ru.nessing.transactions_wallet.Dto.WalletDto;
import ru.nessing.transactions_wallet.entities.Wallet;
import ru.nessing.transactions_wallet.exceptions.InsufficientFundsException;
import ru.nessing.transactions_wallet.exceptions.WalletNotFoundException;
import ru.nessing.transactions_wallet.repositories.WalletRepository;
import ru.nessing.transactions_wallet.utils.OperationType;

import java.util.Optional;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    /* Автопопытка работы с кошельком, если он уже захвачен другим процессом
    и происходит проверка на наличие кошелька и достаточного количества на балансе
    для снятия */
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 1000))
    @Transactional
    public Wallet changeBalance(WalletDto walletDto) {
        Wallet wallet = walletRepository.findById(walletDto.getId())
                .orElseThrow(() -> new WalletNotFoundException("Кошелёк не найден"));

        if (walletDto.getOperation().equals(OperationType.WITHDRAW.getValue()) &&
                wallet.getBalance() < walletDto.getAmount()) {
            throw new InsufficientFundsException("Недостаточно средств");
        }

        return operation(walletDto);
    }

    // непосредственная манипуляция с кошельком
    public Wallet operation(WalletDto walletDto) {
        Optional<Wallet> wallet = walletRepository.findById(walletDto.getId());
        if (wallet.isEmpty()) return null;
        String formatedOperation = walletDto.getOperation().toUpperCase();
        OperationType operationType = OperationType.getOperationsType(formatedOperation);
        if (operationType == null) return null;

        switch (operationType) {
            case DEPOSIT:
                wallet.get().plus(walletDto.getAmount());
                break;
            case WITHDRAW:
                wallet.get().minus(walletDto.getAmount());
                break;
            default: return null;
        }
        walletRepository.save(wallet.get());
        return wallet.get();
    }

    // метод для получения баланса
    public Double getBalance(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Кошелёк не найден"));
        return wallet.getBalance();
    }
}
