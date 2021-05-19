package com.lxy.firenze.holdem.domain;

import com.lxy.firenze.holdem.exception.NoMatchSpreadException;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Arrays;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Face implements Comparable<Face> {

    public enum Type {
        STRAIGHT_FLUSH {
            @Override
            public boolean match(int[] dValue, boolean isFlush) {
                return Arrays.stream(dValue).allMatch(d -> d == 1) && isFlush == true;
            }

            @Override
            public Integer character(Poker.Card[] cards) {
                return cards[0].getValue();
            }
        },
        FOUR_OF_A_KIND {
            @Override
            public boolean match(int[] dValue, boolean isFlush) {
                return dValue[0] == 0 && dValue[1] == 0 && dValue[2] == 0
                        || dValue[1] == 0 && dValue[2] == 0 && dValue[3] == 0;
            }

            @Override
            public Integer character(Poker.Card[] cards) {
                return cards[1].getValue();
            }
        },
        FULL_HOUSE {
            @Override
            public boolean match(int[] dValue, boolean isFlush) {
                return dValue[0] == 0 && dValue[2] == 0 && dValue[3] == 0
                        || dValue[0] == 0 && dValue[1] == 0 && dValue[3] == 0;
            }

            @Override
            public Integer character(Poker.Card[] cards) {
                return Arrays.stream(cards).collect(groupingBy(Poker.Card::getValue, counting()))
                        .entrySet()
                        .stream()
                        .map(e -> e.getKey() * Math.pow(13, e.getValue() - 2))
                        .mapToInt(Double::intValue)
                        .sum();
            }
        },
        FLUSH {
            @Override
            public boolean match(int[] dValue, boolean isFlush) {
                return isFlush == true;
            }

            @Override
            public Integer character(Poker.Card[] cards) {
                return 0;
            }
        },
        STRAIGHT {
            @Override
            public boolean match(int[] dValue, boolean isFlush) {
                return Arrays.stream(dValue).allMatch(d -> d == 1);
            }

            @Override
            public Integer character(Poker.Card[] cards) {
                return cards[0].getValue();
            }
        },
        THREE_OF_A_KIND {
            @Override
            public boolean match(int[] dValue, boolean isFlush) {
                return dValue[0] == 0 && dValue[1] == 0 ||
                        dValue[1] == 0 && dValue[2] == 0 ||
                        dValue[2] == 0 && dValue[3] == 0;
            }

            @Override
            public Integer character(Poker.Card[] cards) {
                return cards[2].getValue();
            }
        },
        TWO_PAIR {
            @Override
            public boolean match(int[] dValue, boolean isFlush) {
                return Arrays.stream(dValue).filter(d -> d == 0).count() == 2;
            }

            @Override
            public Integer character(Poker.Card[] cards) {
                Integer[] values = Arrays.stream(cards).collect(groupingBy(Poker.Card::getValue, counting()))
                        .entrySet()
                        .stream()
                        .filter(e -> e.getValue() == 2)
                        .map(e -> e.getKey())
                        .sorted()
                        .toArray(Integer[]::new);
                return values[0] + 13 * values[1];
            }
        },
        ONE_PAIR {
            @Override
            public boolean match(int[] dValue, boolean isFlush) {
                return Arrays.stream(dValue).filter(d -> d == 0).count() == 1;
            }

            @Override
            public Integer character(Poker.Card[] cards) {
                return Arrays.stream(cards).collect(groupingBy(Poker.Card::getValue, counting()))
                        .entrySet()
                        .stream()
                        .filter(e -> e.getValue() == 2)
                        .map(e -> e.getKey())
                        .findFirst()
                        .get();
            }
        },
        HIGH_CARD {
            @Override
            public boolean match(int[] dValue, boolean isFlush) {
                return true;
            }

            @Override
            public Integer character(Poker.Card[] cards) {
                return cards[4].getValue();
            }
        };

        public abstract Integer character(Poker.Card[] cards);

        public abstract boolean match(int[] dValues, boolean isFlush);
    }

    private Type type;

    private Integer character;

    @Override
    public int compareTo(Face o) {
        if (this.type == o.type) {
            return this.character - o.getCharacter();
        } else {
            return o.getType().ordinal() - this.type.ordinal();
        }
    }

    public static Face of(Poker.Card... cards) {
        int[] dValues = new int[4];
        for (int i = 0; i < 4; i++) {
            dValues[i] = cards[i + 1].getValue() - cards[i].getValue();
        }
        boolean isFlush = Arrays.stream(cards).map(Poker.Card::getSuit).distinct().count() == 1;
        return Arrays.stream(Face.Type.values())
                .filter(t -> t.match(dValues, isFlush))
                .map(t -> new Face(t, t.character(cards)))
                .findFirst()
                .orElseThrow(NoMatchSpreadException::new);
    }
}
