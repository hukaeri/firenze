package com.lxy.firenze.holdem.constant;

import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum PokerSuit {

    CLUB {
        @Override
        public String toString() {
            return "\u2663\uFE0F";
        }
    },
    DIAMOND {
        @Override
        public String toString() {
            return "\u2666\uFE0F";
        }
    },
    HEART {
        @Override
        public String toString() {
            return "\u2665\uFE0F";
        }
    },
    SPADE {
        @Override
        public String toString() {
            return "\u2660\uFE0F";
        }
    };

    private static Map<Integer, PokerSuit> suitMap;

    static {
        suitMap = Stream.of(PokerSuit.values()).collect(Collectors.toMap(PokerSuit::ordinal, p -> p));
    }

    public static PokerSuit of(int ordinal) {
        return suitMap.get(ordinal);
    }

    public static void main(String args[]) {
        System.out.println("\u2660\uFE0F");
    }

}
