package com.lxy.firenze.holdem.service;

import com.lxy.firenze.holdem.domain.Holdem;
import com.lxy.firenze.holdem.domain.Player;
import com.lxy.firenze.holdem.exception.GameInitialException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final DealService dealService;
    private final ActionService actionService;
    private final BonusService bonusService;
    private final SpreadService spreadService;

    public void play(Holdem holdem) {
        while (!holdem.isGameOver()) {
            dealService.deal(holdem);
            while (!holdem.isRoundOver()) {
                actionService.action(holdem);
            }
            holdem.nextRound();
        }
        List<List<Integer>> winners = spreadService.winners(holdem);
        bonusService.share(holdem, winners);
    }

    public Holdem start(List<Player> players) {
        if (CollectionUtils.isEmpty(players) || players.size() < 4) {
            throw new GameInitialException("too few players");
        }
        if (players.size() > 10) {
            throw new GameInitialException("too many players");
        }
        return new Holdem(players);
    }

    public Holdem nextGame(Holdem holdem) {
        // TODO
        return null;
    }
}
