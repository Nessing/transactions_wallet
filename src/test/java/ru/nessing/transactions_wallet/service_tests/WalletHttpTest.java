package ru.nessing.transactions_wallet.service_tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import ru.nessing.transactions_wallet.Dto.WalletDto;
import ru.nessing.transactions_wallet.entities.Wallet;
import ru.nessing.transactions_wallet.repositories.WalletRepository;
import ru.nessing.transactions_wallet.services.WalletService;
import ru.nessing.transactions_wallet.utils.OperationType;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WalletHttpTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private WalletService walletService;

    private final double amount = 1000.0;
    private UUID walletId = null;

    @BeforeEach
    public void createWallet() {
        Wallet wallet = Wallet.builder()
                .balance(this.amount)
                .build();

        walletRepository.save(wallet);
        this.walletId = wallet.getId();
    }

    @Test
    public void testGetBalance() {
        ResponseEntity<?> response = restTemplate.getForEntity("/api/v1/wallet/" + this.walletId, Object.class);
        System.out.println(response.getBody());
        assertEquals(amount, response.getBody());
    }

    @Test
    public void testUpdateWalletBalance() {
        double deposit = 150.0;
        WalletDto walletDto = WalletDto.builder()
                .id(this.walletId)
                .operation(OperationType.DEPOSIT.getValue())
                .amount(deposit)
                .build();

        restTemplate.postForEntity("/api/v1/wallet/update", walletDto, Void.class);

        double checkingBalance = this.amount + deposit;
        double updateBalance = walletService.getBalance(this.walletId);

        System.out.printf("checking balance: %f\nupdated balance: %f\n", checkingBalance, updateBalance);
        assertEquals(checkingBalance, updateBalance);
    }
}
