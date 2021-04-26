package com.lxy.firenze.holdem.service;

import com.lxy.firenze.holdem.domain.Holdem;
import org.springframework.stereotype.Service;

@Service
public class DealService {

    public void deal(Holdem holdem) {
        if (holdem.getRound() == 1) {
            holdem.preFlop();
        } else if (holdem.getRound() == 2) {
            holdem.flop();
        } else if (holdem.getRound() == 3) {
            holdem.turn();
        } else {
            holdem.river();
        }
    }
}
