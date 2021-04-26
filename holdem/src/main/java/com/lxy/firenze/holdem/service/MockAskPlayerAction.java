package com.lxy.firenze.holdem.service;

import com.lxy.firenze.holdem.domain.Player;
import com.lxy.firenze.holdem.domain.PlayerAction;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "holdem.ask.player", havingValue = "mock", matchIfMissing = true)
public class MockAskPlayerAction implements AskPlayerAction {

    @Override
    public PlayerAction ask(Player player, int minAmount) {
        return new PlayerAction.Bet();
    }
}
