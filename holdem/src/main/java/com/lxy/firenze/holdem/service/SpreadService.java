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

@Service
public class SpreadService {

    public List<List<Integer>> winners(Holdem holdem) {
        Spread[] spreads = IntStream.range(0, holdem.getPlayerCount())
                .mapToObj(i -> holdem.getPlayerCards()[i])
                .map(playerCards -> spread(playerCards, holdem.getPublicCards()))
                .toArray(Spread[]::new);
        List<List<Integer>> winners = new ArrayList<>();
        winners.add(IntStream.range(0, holdem.getPlayerCount())
                .boxed()
                .collect(Collectors.groupingBy(i -> spreads[i], Collectors.toList()))
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList())
                .get(holdem.getPlayerCount() - 1));
        // TODO all in
        return winners;
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
