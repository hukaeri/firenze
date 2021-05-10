package com.lxy.firenze.holdem.service;

import com.lxy.firenze.holdem.domain.Holdem;
import org.springframework.stereotype.Service;

@Service
public class DealService {
    public void deal(Holdem holdem) {
        holdem.getRound().deal(holdem);
    }
}
