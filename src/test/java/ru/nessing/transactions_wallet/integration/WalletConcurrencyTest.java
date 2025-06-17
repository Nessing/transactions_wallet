package ru.nessing.transactions_wallet.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.nessing.transactions_wallet.Dto.WalletDto;
import ru.nessing.transactions_wallet.entities.Wallet;
import ru.nessing.transactions_wallet.repositories.WalletRepository;
import ru.nessing.transactions_wallet.services.WalletService;
import ru.nessing.transactions_wallet.utils.OperationType;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WalletConcurrencyTest {

    private int finalBalance = 0;

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    @Test
    void testRaceCondition() throws ExecutionException, InterruptedException {

        Wallet wallet = Wallet.builder()
                .balance(1000.0)
                .build();

        walletRepository.save(wallet);
        UUID walletId = wallet.getId();
        this.finalBalance += wallet.getBalance();

        int countThreads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(countThreads);

        // поток для имитации обращения к одному кошельку
        Runnable task = () -> {
            WalletDto dto = WalletDto.builder()
                    .id(walletId)
                    .build();

            double randomAmount = Math.floor(Math.random() * 200);

            int randomOperationRange = ThreadLocalRandom.current().nextInt(0, 2);
            String randomOperation = OperationType.values()[randomOperationRange].toString();

            /** Если необходимо проверить исключение
            при попытке снять с кошелька больше, чем имеется **/
            /* dto.setOperation(OperationType.WITHDRAW.getValue());
            dto.setAmount(2000.0);
            this.finalBalance -= dto.getAmount(); */

            dto.setOperation(randomOperation);
            dto.setAmount(randomAmount);
            if (randomOperation.equals(OperationType.DEPOSIT.getValue())) {
                this.finalBalance += dto.getAmount();
            } else if (randomOperation.equals(OperationType.WITHDRAW.getValue())) {
                this.finalBalance -= dto.getAmount();
            }

            walletService.changeBalance(dto);
        };
        for (int i = 0; i < countThreads; i++) {
            executorService.submit(task).get();
        }

        Double WalletBalance = walletService.getBalance(walletId);
        System.out.printf("UUID wallet: %s\nBalance: %s\n", walletId, WalletBalance);
        assertEquals(this.finalBalance, WalletBalance);
    }
}
