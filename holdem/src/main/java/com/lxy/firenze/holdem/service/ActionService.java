package com.lxy.firenze.holdem.service;

import com.lxy.firenze.holdem.domain.Holdem;
import com.lxy.firenze.holdem.domain.Player;
import com.lxy.firenze.holdem.domain.PlayerAction;
import com.lxy.firenze.holdem.event.PlayerActionEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActionService {

    private final AskPlayerAction askPlayerAction;

    private final ApplicationEventPublisher applicationEventPublisher;

    public void action(Holdem holdem) {
        PlayerAction action;
//        if (holdem.isSmallBlindTurn()) {
//            action = new PlayerAction.Bet();
//        } else if (holdem.isBigBlindTurn()) {
//            action = new PlayerAction.Raise();
//        } else {
            Player player = holdem.currentActionPlayer();
            action = askPlayerAction.ask(player, holdem.getMinBetAmount());
//        }
        applicationEventPublisher.publishEvent(new PlayerActionEvent(holdem, action));
    }
}
