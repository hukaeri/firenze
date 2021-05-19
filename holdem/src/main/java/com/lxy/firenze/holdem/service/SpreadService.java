package com.lxy.firenze.holdem.service;

import com.lxy.firenze.holdem.domain.Face;
import com.lxy.firenze.holdem.domain.Holdem;
import com.lxy.firenze.holdem.domain.Poker;
import com.lxy.firenze.holdem.domain.Round;
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
        holdem.getAllInMapMap().values().forEach(c -> {
            c.addAll(holdem.finalPlayers());
            winners.add(contest(holdem, c));
        });
        if (!holdem.getAllInMapMap().containsKey(Round.RIVER)) {
            winners.add(contest(holdem, holdem.finalPlayers()));
        }
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

    private Face spread(Poker.Card[] playerCards, Poker.Card[] publicCards) {
        List<Poker.Card> sortedList = Stream.concat(Arrays.stream(playerCards), Arrays.stream(publicCards))
                .sorted(Comparator.comparingInt(Poker.Card::getValue))
                .collect(Collectors.toList());
        return IntStream.range(0, 3)
                .mapToObj(i -> sortedList.subList(i, i + 5).toArray(new Poker.Card[5]))
                .map(cards -> Face.of(cards))
                .sorted(Face::compareTo)
                .sorted(Comparator.reverseOrder())
                .findFirst()
                .get();
    }

}
