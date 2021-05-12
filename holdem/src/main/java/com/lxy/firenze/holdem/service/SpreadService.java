package com.lxy.firenze.holdem.service;

import com.lxy.firenze.holdem.domain.Holdem;
import com.lxy.firenze.holdem.domain.Poker;
import com.lxy.firenze.holdem.domain.Spread;
import com.lxy.firenze.holdem.exception.NoMatchSpreadException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class SpreadService {

    public List<List<Integer>> winners(Holdem holdem) {
        List<List<Integer>> winners = new ArrayList<>();
        holdem.getRaiseMap().values().forEach(c -> {
            c.addAll(holdem.finalPlayers());
            winners.add(contest(holdem, c));
        });
        winners.add(contest(holdem, holdem.finalPlayers()));
        return winners;
    }

    private List<Integer> contest(Holdem holdem, List<Integer> players) {
        return players.stream()
                .collect(groupingBy(p -> spread(holdem.getPlayerCards()[p], holdem.getPublicCards()), toList()))
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .reduce((first, second) -> second)
                .get();
    }

    private Spread spread(Poker.Card[] contestCards) {
        int[] dValues = new int[4];
        for (int i = 0; i < 4; i++) {
            dValues[i] = contestCards[i + 1].getValue() - contestCards[i].getValue();
        }
        boolean isFlush = Arrays.stream(contestCards).map(Poker.Card::getSuit).distinct().count() == 1;
        Spread.Type[] types = Spread.Type.values();
        for (int i = 0; i < types.length; i++) {
            if (types[i].match(dValues, isFlush)) {
                return new Spread(types[i], types[i].character(contestCards));
            }
        }
        throw new NoMatchSpreadException();
    }

    private Spread spread(Poker.Card[] playerCards, Poker.Card[] publicCards) {
        List<Poker.Card> sortedList = Stream.concat(Arrays.stream(playerCards), Arrays.stream(publicCards))
                .sorted(Comparator.comparingInt(Poker.Card::getValue))
                .collect(Collectors.toList());
        return IntStream.range(0, 3)
                .mapToObj(i -> sortedList.subList(i, i + 5).toArray(new Poker.Card[5]))
                .map(cards -> spread(cards))
                .sorted(Comparator.reverseOrder())
                .findFirst()
                .get();
    }

}
