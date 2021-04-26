package com.lxy.firenze.holdem.constant;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum PokerSuit {

    CLUB, DIAMOND, HEART, SPADE;

    private static Map<Integer, PokerSuit> suitMap;

    static {
        suitMap = Stream.of(PokerSuit.values()).collect(Collectors.toMap(PokerSuit::ordinal, p -> p));
    }

    public static PokerSuit of(int ordinal) {
        return suitMap.get(ordinal);
    }

}
