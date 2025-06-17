package ru.nessing.transactions_wallet.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.nessing.transactions_wallet.Dto.WalletDto;
import ru.nessing.transactions_wallet.entities.Wallet;
import ru.nessing.transactions_wallet.services.WalletService;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/wallet")
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/update")
    public Wallet updateWallet(@Valid @RequestBody WalletDto walletDto) {
        return walletService.changeBalance(walletDto);
    }

    @GetMapping("/{walletId}")
    public Double getBalance(@Valid @PathVariable UUID walletId) {
        return walletService.getBalance(walletId);
    }
}
