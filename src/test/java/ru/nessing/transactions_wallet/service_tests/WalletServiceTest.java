package ru.nessing.transactions_wallet.service_tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nessing.transactions_wallet.entities.Wallet;
import ru.nessing.transactions_wallet.repositories.WalletRepository;
import ru.nessing.transactions_wallet.services.WalletService;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    private final UUID UUID_TEST = UUID.randomUUID();

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @Test
    public void test() {
        Wallet wallet = new Wallet();
        wallet.setId(UUID_TEST);
        wallet.setBalance(900.0);

        // прописывание поведения заглушки
        Mockito.when(walletRepository.findById(UUID_TEST)).thenReturn(Optional.of(wallet));

        // выполнение теста для получения баланса по UUID
        Assertions.assertEquals(wallet.getBalance(), walletService.getBalance(UUID_TEST));
        System.out.println(wallet.getBalance());
    }
}
