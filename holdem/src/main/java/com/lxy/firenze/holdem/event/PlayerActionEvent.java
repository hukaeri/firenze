package com.lxy.firenze.holdem.event;

import com.lxy.firenze.holdem.domain.Holdem;
import com.lxy.firenze.holdem.domain.PlayerAction;
import com.lxy.firenze.holdem.exception.UnsupportActionException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerActionEvent {

    private Holdem holdem;

    private PlayerAction playerAction;

    public void doAction() {
        if (!playerAction.validate(holdem)) {
            throw new UnsupportActionException();
        }
        playerAction.action(holdem);
    }
}
