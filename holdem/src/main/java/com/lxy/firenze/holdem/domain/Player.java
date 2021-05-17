package com.lxy.firenze.holdem.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Player {

    private String name;

    private Integer balance;

    public void decreaseAmount(int amount) {
        balance -= amount;
    }

    public void increaseBalance(int amount) {
        balance += amount;
    }

    public String toString() {
        return name + ":" + balance;
    }
}
