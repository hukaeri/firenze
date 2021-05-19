package com.lxy.firenze.holdem.domain;

import com.lxy.firenze.holdem.constant.PokerSuit;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FaceTest {

    @Test
    public void should_spade_23456_less_than_spade_78910J() {
        Face spade_23456 = Face.of(new Poker.Card(PokerSuit.SPADE, "2"),
                new Poker.Card(PokerSuit.SPADE, "3"),
                new Poker.Card(PokerSuit.SPADE, "4"),
                new Poker.Card(PokerSuit.SPADE, "5"),
                new Poker.Card(PokerSuit.SPADE, "6"));
        Face spade_78910J = Face.of(new Poker.Card(PokerSuit.SPADE, "7"),
                new Poker.Card(PokerSuit.SPADE, "8"),
                new Poker.Card(PokerSuit.SPADE, "9"),
                new Poker.Card(PokerSuit.SPADE, "10"),
                new Poker.Card(PokerSuit.SPADE, "J"));

        assertThat(spade_23456.getType()).isEqualTo(Face.Type.STRAIGHT_FLUSH);
        assertThat(spade_78910J.getType()).isEqualTo(Face.Type.STRAIGHT_FLUSH);
        assertThat(spade_23456.compareTo(spade_78910J)).isLessThan(0);
    }

    @Test
    public void should_2222_less_than_3333() {
        Face four_2222 = Face.of(new Poker.Card(PokerSuit.SPADE, "2"),
                new Poker.Card(PokerSuit.HEART, "2"),
                new Poker.Card(PokerSuit.DIAMOND, "2"),
                new Poker.Card(PokerSuit.CLUB, "2"),
                new Poker.Card(PokerSuit.SPADE, "6"));
        Face four_3333 = Face.of(new Poker.Card(PokerSuit.SPADE, "3"),
                new Poker.Card(PokerSuit.HEART, "3"),
                new Poker.Card(PokerSuit.DIAMOND, "3"),
                new Poker.Card(PokerSuit.CLUB, "3"),
                new Poker.Card(PokerSuit.SPADE, "J"));

        assertThat(four_2222.getType()).isEqualTo(Face.Type.FOUR_OF_A_KIND);
        assertThat(four_3333.getType()).isEqualTo(Face.Type.FOUR_OF_A_KIND);
        assertThat(four_2222.compareTo(four_3333)).isLessThan(0);
    }

    @Test
    public void should_222_44_less_than_333_55() {
        Face three_222_44 = Face.of(new Poker.Card(PokerSuit.SPADE, "2"),
                new Poker.Card(PokerSuit.HEART, "2"),
                new Poker.Card(PokerSuit.DIAMOND, "2"),
                new Poker.Card(PokerSuit.SPADE, "4"),
                new Poker.Card(PokerSuit.DIAMOND, "4"));
        Face three_333_55 = Face.of(new Poker.Card(PokerSuit.SPADE, "3"),
                new Poker.Card(PokerSuit.HEART, "3"),
                new Poker.Card(PokerSuit.DIAMOND, "3"),
                new Poker.Card(PokerSuit.SPADE, "5"),
                new Poker.Card(PokerSuit.DIAMOND, "5"));

        assertThat(three_222_44.getType()).isEqualTo(Face.Type.FULL_HOUSE);
        assertThat(three_333_55.getType()).isEqualTo(Face.Type.FULL_HOUSE);
        assertThat(three_222_44.compareTo(three_333_55)).isLessThan(0);
    }

    @Test
    public void should_333_44_less_than_333_55() {
        Face three_333_44 = Face.of(new Poker.Card(PokerSuit.SPADE, "3"),
                new Poker.Card(PokerSuit.HEART, "3"),
                new Poker.Card(PokerSuit.DIAMOND, "3"),
                new Poker.Card(PokerSuit.SPADE, "4"),
                new Poker.Card(PokerSuit.DIAMOND, "4"));
        Face three_333_55 = Face.of(new Poker.Card(PokerSuit.SPADE, "3"),
                new Poker.Card(PokerSuit.HEART, "3"),
                new Poker.Card(PokerSuit.DIAMOND, "3"),
                new Poker.Card(PokerSuit.SPADE, "5"),
                new Poker.Card(PokerSuit.DIAMOND, "5"));

        assertThat(three_333_44.getType()).isEqualTo(Face.Type.FULL_HOUSE);
        assertThat(three_333_55.getType()).isEqualTo(Face.Type.FULL_HOUSE);
        assertThat(three_333_44.compareTo(three_333_55)).isLessThan(0);
    }

    @Test
    public void should_flush_equals() {
        Face flush1 = Face.of(new Poker.Card(PokerSuit.SPADE, "2"),
                new Poker.Card(PokerSuit.SPADE, "4"),
                new Poker.Card(PokerSuit.SPADE, "6"),
                new Poker.Card(PokerSuit.SPADE, "8"),
                new Poker.Card(PokerSuit.SPADE, "10"));
        Face flush2 = Face.of(new Poker.Card(PokerSuit.SPADE, "3"),
                new Poker.Card(PokerSuit.SPADE, "5"),
                new Poker.Card(PokerSuit.SPADE, "7"),
                new Poker.Card(PokerSuit.SPADE, "9"),
                new Poker.Card(PokerSuit.SPADE, "J"));

        assertThat(flush1.getType()).isEqualTo(Face.Type.FLUSH);
        assertThat(flush2.getType()).isEqualTo(Face.Type.FLUSH);
        assertThat(flush1.compareTo(flush2)).isEqualTo(0);
    }

    @Test
    public void should_23456_less_than_78910J() {
        Face straight_23456 = Face.of(new Poker.Card(PokerSuit.SPADE, "2"),
                new Poker.Card(PokerSuit.HEART, "3"),
                new Poker.Card(PokerSuit.SPADE, "4"),
                new Poker.Card(PokerSuit.CLUB, "5"),
                new Poker.Card(PokerSuit.DIAMOND, "6"));
        Face straight_78910J = Face.of(new Poker.Card(PokerSuit.SPADE, "7"),
                new Poker.Card(PokerSuit.CLUB, "8"),
                new Poker.Card(PokerSuit.HEART, "9"),
                new Poker.Card(PokerSuit.HEART, "10"),
                new Poker.Card(PokerSuit.SPADE, "J"));

        assertThat(straight_23456.getType()).isEqualTo(Face.Type.STRAIGHT);
        assertThat(straight_78910J.getType()).isEqualTo(Face.Type.STRAIGHT);
        assertThat(straight_23456.compareTo(straight_78910J)).isLessThan(0);
    }

    @Test
    public void should_222_less_than_333() {
        Face three_222 = Face.of(new Poker.Card(PokerSuit.SPADE, "2"),
                new Poker.Card(PokerSuit.HEART, "2"),
                new Poker.Card(PokerSuit.CLUB, "2"),
                new Poker.Card(PokerSuit.CLUB, "5"),
                new Poker.Card(PokerSuit.DIAMOND, "6"));
        Face three_333 = Face.of(new Poker.Card(PokerSuit.SPADE, "3"),
                new Poker.Card(PokerSuit.CLUB, "3"),
                new Poker.Card(PokerSuit.HEART, "3"),
                new Poker.Card(PokerSuit.HEART, "10"),
                new Poker.Card(PokerSuit.SPADE, "J"));

        assertThat(three_222.getType()).isEqualTo(Face.Type.THREE_OF_A_KIND);
        assertThat(three_333.getType()).isEqualTo(Face.Type.THREE_OF_A_KIND);
        assertThat(three_222.compareTo(three_333)).isLessThan(0);
    }

    @Test
    public void should_22_33_less_than_44_55() {
        Face two_22_33 = Face.of(new Poker.Card(PokerSuit.SPADE, "2"),
                new Poker.Card(PokerSuit.HEART, "2"),
                new Poker.Card(PokerSuit.CLUB, "3"),
                new Poker.Card(PokerSuit.SPADE, "3"),
                new Poker.Card(PokerSuit.DIAMOND, "6"));
        Face two_44_55 = Face.of(new Poker.Card(PokerSuit.SPADE, "4"),
                new Poker.Card(PokerSuit.CLUB, "4"),
                new Poker.Card(PokerSuit.HEART, "5"),
                new Poker.Card(PokerSuit.SPADE, "5"),
                new Poker.Card(PokerSuit.SPADE, "J"));

        assertThat(two_22_33.getType()).isEqualTo(Face.Type.TWO_PAIR);
        assertThat(two_44_55.getType()).isEqualTo(Face.Type.TWO_PAIR);
        assertThat(two_22_33.compareTo(two_44_55)).isLessThan(0);
    }

    @Test
    public void should_22_33_less_than_22_55() {
        Face two_22_33 = Face.of(new Poker.Card(PokerSuit.SPADE, "2"),
                new Poker.Card(PokerSuit.HEART, "2"),
                new Poker.Card(PokerSuit.CLUB, "3"),
                new Poker.Card(PokerSuit.SPADE, "3"),
                new Poker.Card(PokerSuit.DIAMOND, "6"));
        Face two_22_55 = Face.of(new Poker.Card(PokerSuit.SPADE, "2"),
                new Poker.Card(PokerSuit.HEART, "2"),
                new Poker.Card(PokerSuit.HEART, "5"),
                new Poker.Card(PokerSuit.SPADE, "5"),
                new Poker.Card(PokerSuit.SPADE, "J"));

        assertThat(two_22_33.getType()).isEqualTo(Face.Type.TWO_PAIR);
        assertThat(two_22_55.getType()).isEqualTo(Face.Type.TWO_PAIR);
        assertThat(two_22_33.compareTo(two_22_55)).isLessThan(0);
    }

    @Test
    public void should_22_less_than_33() {
        Face two_22 = Face.of(new Poker.Card(PokerSuit.SPADE, "2"),
                new Poker.Card(PokerSuit.HEART, "2"),
                new Poker.Card(PokerSuit.CLUB, "4"),
                new Poker.Card(PokerSuit.CLUB, "5"),
                new Poker.Card(PokerSuit.DIAMOND, "6"));
        Face two_33 = Face.of(new Poker.Card(PokerSuit.SPADE, "3"),
                new Poker.Card(PokerSuit.CLUB, "3"),
                new Poker.Card(PokerSuit.HEART, "8"),
                new Poker.Card(PokerSuit.HEART, "10"),
                new Poker.Card(PokerSuit.SPADE, "J"));

        assertThat(two_22.getType()).isEqualTo(Face.Type.ONE_PAIR);
        assertThat(two_33.getType()).isEqualTo(Face.Type.ONE_PAIR);
        assertThat(two_22.compareTo(two_33)).isLessThan(0);
    }

    @Test
    public void should_2_less_than_3() {
        Face high_k = Face.of(new Poker.Card(PokerSuit.SPADE, "2"),
                new Poker.Card(PokerSuit.HEART, "3"),
                new Poker.Card(PokerSuit.CLUB, "5"),
                new Poker.Card(PokerSuit.CLUB, "7"),
                new Poker.Card(PokerSuit.DIAMOND, "K"));
        Face high_A = Face.of(new Poker.Card(PokerSuit.SPADE, "3"),
                new Poker.Card(PokerSuit.CLUB, "4"),
                new Poker.Card(PokerSuit.HEART, "6"),
                new Poker.Card(PokerSuit.HEART, "9"),
                new Poker.Card(PokerSuit.SPADE, "A"));

        assertThat(high_k.getType()).isEqualTo(Face.Type.HIGH_CARD);
        assertThat(high_A.getType()).isEqualTo(Face.Type.HIGH_CARD);
        assertThat(high_k.compareTo(high_A)).isLessThan(0);
    }
}
