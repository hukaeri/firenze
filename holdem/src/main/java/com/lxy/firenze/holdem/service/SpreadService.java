package com.lxy.firenze.holdem.service;

import com.lxy.firenze.holdem.constant.PokerSuit;
import com.lxy.firenze.holdem.domain.Holdem;
import com.lxy.firenze.holdem.domain.Poker;
import com.lxy.firenze.holdem.domain.Round;
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

import static java.util.stream.Collectors.*;

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
        // System.out.println(sortedList.stream().map(c -> c.toString()).collect(joining(",")));
        return IntStream.range(0, 3)
                .mapToObj(i -> sortedList.subList(i, i + 5).toArray(new Poker.Card[5]))
                .map(cards -> spread(cards))
                // .peek(s -> System.out.println(s.getType() + "-" + s.getCharacter()))
                .sorted(Spread::compareTo)
                .sorted(Comparator.reverseOrder())
                .findFirst()
                .get();
    }

    public static void main(String[] args) {
        Poker.Card[] playerCards = new Poker.Card[2];
        playerCards[0] = new Poker.Card(PokerSuit.DIAMOND, 12);
        playerCards[1] = new Poker.Card(PokerSuit.CLUB, 3);

        Poker.Card[] playerCard2s = new Poker.Card[2];
        playerCard2s[0] = new Poker.Card(PokerSuit.DIAMOND, 11);
        playerCard2s[1] = new Poker.Card(PokerSuit.HEART, 8);

        Poker.Card[] playerCard3s = new Poker.Card[2];
        playerCard3s[0] = new Poker.Card(PokerSuit.CLUB, 8);
        playerCard3s[1] = new Poker.Card(PokerSuit.HEART, 12);

        Poker.Card[] playerCard4s = new Poker.Card[2];
        playerCard4s[0] = new Poker.Card(PokerSuit.HEART, 5);
        playerCard4s[1] = new Poker.Card(PokerSuit.DIAMOND, 1);

        Poker.Card[] publicCards = new Poker.Card[5];
        publicCards[0] = new Poker.Card(PokerSuit.HEART, 2);
        publicCards[1] = new Poker.Card(PokerSuit.DIAMOND, 8);
        publicCards[2] = new Poker.Card(PokerSuit.CLUB, 5);
        publicCards[3] = new Poker.Card(PokerSuit.SPADE, 3);
        publicCards[4] = new Poker.Card(PokerSuit.SPADE, 10);

        SpreadService spreadService = new SpreadService();
        Spread spread1 = spreadService.spread(playerCards, publicCards);
        System.out.println(spread1.getType() + "-" + spread1.getCharacter());
        Spread spread2 = spreadService.spread(playerCard2s, publicCards);
        System.out.println(spread2.getType() + "-" + spread2.getCharacter());
        Spread spread3 = spreadService.spread(playerCard3s, publicCards);
        System.out.println(spread3.getType() + "-" + spread3.getCharacter());
        Spread spread4 = spreadService.spread(playerCard4s, publicCards);
        System.out.println(spread4.getType() + "-" + spread4.getCharacter());
    }

}
