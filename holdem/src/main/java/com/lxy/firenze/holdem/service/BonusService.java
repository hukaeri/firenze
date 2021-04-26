package com.lxy.firenze.holdem.service;

import com.lxy.firenze.holdem.domain.Holdem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BonusService {

    public void share(Holdem holdem, List<List<Integer>> winners) {
        holdem.share(winners);
    }

}
