package com.lxy.firenze.holdem.domain;

import com.lxy.firenze.holdem.constant.PokerSuit;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class Poker {

    private static final int POKER_SIZE = 52;

    @AllArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class Card {

        private PokerSuit suit;

        private Integer value;
    }

    private LinkedList<Card> cards;

    public Poker() {
        cards = new LinkedList<>();
        new Random().ints(0, POKER_SIZE)
                .distinct()
                .limit(POKER_SIZE)
                .forEach(i -> cards.add(new Card(PokerSuit.of(i / 13), i % 13 + 1)));
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
