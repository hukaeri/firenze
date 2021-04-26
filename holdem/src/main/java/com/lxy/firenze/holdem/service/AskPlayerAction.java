package com.lxy.firenze.holdem.service;

import com.lxy.firenze.holdem.domain.Player;
import com.lxy.firenze.holdem.domain.PlayerAction;

public interface AskPlayerAction {

    PlayerAction ask(Player player, int minAmount);
}
