package ru.nessing.transactions_wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class TransactionsWalletApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionsWalletApplication.class, args);
    }

}
