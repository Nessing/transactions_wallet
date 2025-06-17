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
import ru.nessing.transactions_wallet.utils.OperationType;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WalletHttpTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WalletRepository walletRepository;

    private UUID walletId = null;

    @BeforeEach
    public void createWallet() {
        Wallet wallet = Wallet.builder()
                .balance(1000.0)
                .build();

        walletRepository.save(wallet);
        this.walletId = wallet.getId();
    }

    @Test
    public void testGetBalance() {
        ResponseEntity<?> response = restTemplate.getForEntity("/api/v1/wallet/" + this.walletId, Object.class);
        System.out.println(response.getBody());
    }

    @Test
    public void testUpdateWalletBalance() {
        WalletDto walletDto = WalletDto.builder()
                .id(this.walletId)
                .operation(OperationType.DEPOSIT.getValue())
                .amount(1000.0)
                .build();

        ResponseEntity<?> response = restTemplate.postForEntity("/api/v1/wallet/update", walletDto, Object.class);
        System.out.println(response.getBody());
    }
}
