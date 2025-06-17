package ru.nessing.transactions_wallet.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.nessing.transactions_wallet.entities.Wallet;
import ru.nessing.transactions_wallet.services.WalletService;

import java.util.UUID;

@SpringBootTest
public class WalletServiceTest {

    private final UUID UUID_TEST = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

    @Autowired
    private WalletService walletService;

//    @BeforeEach
//    void setUp() {
//        Wallet wallet = new Wallet();
//        wallet.setId(1L);
//        wallet.setBalance(1000.0);
//        walletRepository.save(wallet);
//    }

    @Test
    public void test() {
        Wallet wallet = new Wallet();
        wallet.setId(UUID_TEST);
        wallet.setBalance(1000.0);

        Assertions.assertEquals(wallet.getBalance(), walletService.getBalance(UUID_TEST));
        System.out.println(wallet);
    }
}
