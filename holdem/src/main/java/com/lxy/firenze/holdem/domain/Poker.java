package com.lxy.firenze.holdem.domain;

import com.lxy.firenze.holdem.constant.PokerSuit;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Poker {

    private static final int POKER_SIZE = 52;

    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    public static class Card {

        private static Map<Integer, String> cardStrMap = new HashMap<>();

        static {
            cardStrMap.put(1, "2");
            cardStrMap.put(2, "3");
            cardStrMap.put(3, "4");
            cardStrMap.put(4, "5");
            cardStrMap.put(5, "6");
            cardStrMap.put(6, "7");
            cardStrMap.put(7, "8");
            cardStrMap.put(8, "9");
            cardStrMap.put(9, "10");
            cardStrMap.put(10, "J");
            cardStrMap.put(11, "Q");
            cardStrMap.put(12, "K");
            cardStrMap.put(13, "A");
        }

        private PokerSuit suit;

        private Integer value;

        public Card(PokerSuit suit, String card) {
            int value = cardStrMap.entrySet().stream()
                    .filter(e -> e.getValue().equals(card))
                    .map(e -> e.getKey())
                    .findFirst()
                    .get();
            this.suit = suit;
            this.value = value;
        }

        @Override
        public String toString() {
            return suit + cardStrMap.get(value);
        }

    }

    private LinkedList<Card> cards;

    public Poker() {
        cards = new Random().ints(0, POKER_SIZE)
                .distinct()
                .limit(POKER_SIZE)
                .mapToObj(i -> new Card(PokerSuit.of(i / 13), i % 13 + 1))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public void cut() {
        cards.removeLast();
    }

    public Card deal() {
        return cards.pop();
    }
}
