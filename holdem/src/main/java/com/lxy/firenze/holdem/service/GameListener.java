package com.lxy.firenze.holdem.service;

import com.lxy.firenze.holdem.event.PlayerActionEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class GameListener {

    @EventListener(PlayerActionEvent.class)
    public void onPlayerAction(PlayerActionEvent playerActionEvent){
        playerActionEvent.doAction();
    }
}
