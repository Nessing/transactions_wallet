package ru.nessing.transactions_wallet.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "balance")
    private Double balance;

    @Version
    private int version;

    public void plus(Double amount) {
        this.balance += amount;
    }
    public void minus(Double amount) {
        this.balance -= amount;
    }
}
