package com.lxy.firenze.holdem.domain;

import com.lxy.firenze.holdem.constant.PlayerActionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Holdem {

    private static final int PUBLIC_CARD_COUNT = 5;
    private static final int PLAYER_CARD_COUNT = 2;
    private static final int CONTEST_CARD_COUNT = 5;
    private static final int ROUNDS = 4;
    private static final int MIN_BET_AMOUNT = 10;
    private static final int RAISE_AMOUNT = 10;

    private Integer playerCount;
    private Poker poker;
    private Player[] players;
    private Poker.Card[][] playerCards;
    private Poker.Card[] publicCards;
    private Poker.Card[][] contestCards;
    private int round;
    private int button;
    private PricePool pricePool;

    private Integer indexOfCurrentPlayer;
    private int currentMinBetAmount;
    private Queue<Integer> waitingPlayers;
    private List<Integer> quitPlayers;
    private PlayerActionType[] playersLastAction;
    private int raiseCount;

    public Holdem(List<Player> players) {
        poker = new Poker();
        playerCount = players.size();
        this.players = players.toArray(new Player[playerCount]);
        playerCards = new Poker.Card[playerCount][PLAYER_CARD_COUNT];
        publicCards = new Poker.Card[PUBLIC_CARD_COUNT];
        contestCards = new Poker.Card[playerCount][CONTEST_CARD_COUNT];
        round = 1;
        button = playerCount - 1;
        pricePool = new PricePool(playerCount);
        pricePool.addPricePool(round);

        indexOfCurrentPlayer = 0;
        currentMinBetAmount = MIN_BET_AMOUNT;
        waitingPlayers = new LinkedList<>();
        IntStream.range(1, playerCount).forEach(i -> waitingPlayers.add(i));
        quitPlayers = new ArrayList<>();
        playersLastAction = new PlayerActionType[playerCount];
    }

    public boolean isGameOver() {
        return quitPlayers.size() == playerCount || round == ROUNDS + 1;
    }

    public boolean isRoundOver() {
        return indexOfCurrentPlayer == null;
    }

    public Player currentActionPlayer() {
        return players[indexOfCurrentPlayer];
    }

    public boolean isSmallBlindTurn() {
        return round == 1 && indexOfCurrentPlayer == 1;
    }

    public boolean isBigBlindTurn() {
        return round == 1 && indexOfCurrentPlayer == 2;
    }

    public void nextRound() {
        clearPlayerBalance();
        round++;
        if (round == ROUNDS + 1) {
            return;
        }
        indexOfCurrentPlayer = 0;
        while ((quitPlayers.contains(indexOfCurrentPlayer)
                || playersLastAction[indexOfCurrentPlayer] == PlayerActionType.ALLIN)
                && indexOfCurrentPlayer < playerCount) {
            indexOfCurrentPlayer++;
        }
        currentMinBetAmount = MIN_BET_AMOUNT;
        waitingPlayers.clear();
        IntStream.range(0, playerCount)
                .filter(i -> playersLastAction[i] != PlayerActionType.ALLIN
                        && playersLastAction[i] != PlayerActionType.FOLD)
                .filter(i -> i != indexOfCurrentPlayer)
                .forEach(waitingPlayers::add);
        IntStream.range(0, playerCount)
                .filter(i -> playersLastAction[i] != PlayerActionType.FOLD
                        && playersLastAction[i] != PlayerActionType.ALLIN)
                .forEach(i -> playersLastAction[i] = null);
        pricePool.addPricePool(round);
        raiseCount = 0;
    }

    private void clearPlayerBalance() {
        for (int i = 0; i < playerCount; i++) {
            players[i].reduceBalance(pricePool.getPlayerAmountInARound(i, round));
        }
    }

    public int getMinBetAmount() {
        return currentMinBetAmount;
    }

    public int[][] getPools() {
        return pricePool.toArray();
    }

    public int getRound() {
        return round;
    }

    public void preFlop() {
        poker.shuffle();
        for (int i = 0; i < playerCount; i++) {
            playerCards[i][0] = poker.deal();
            playerCards[i][1] = poker.deal();
        }
    }

    public void flop() {
        poker.cut();
        for (int i = 0; i < 3; i++) {
            publicCards[i] = poker.deal();
        }
    }

    public void turn() {
        poker.cut();
        publicCards[3] = poker.deal();
    }

    public void river() {
        publicCards[4] = poker.deal();
    }

    private boolean isFirstInRound() {
        return quitPlayers.size() + waitingPlayers.size() + allInPlayerCount() == playerCount - 1;
    }

    private long allInPlayerCount() {
        return Arrays.stream(playersLastAction).filter(a -> a == PlayerActionType.ALLIN).count();
    }

    public boolean canPlayerBet() {
        return currentActionPlayer().getBalance() >= currentMinBetAmount;
    }

    public void onPlayerBet() {
        pricePool.bet(indexOfCurrentPlayer, round, currentMinBetAmount, isFirstInRound());
        playersLastAction[indexOfCurrentPlayer] = PlayerActionType.BET;
        indexOfCurrentPlayer = waitingPlayers.poll();
    }

    public void onPlayerFold() {
        playersLastAction[indexOfCurrentPlayer] = PlayerActionType.FOLD;
        quitPlayers.add(indexOfCurrentPlayer);
        indexOfCurrentPlayer = waitingPlayers.poll();
    }

    public boolean canPlayerRaise() {
        return currentActionPlayer().getBalance() >= currentMinBetAmount
                && !isFirstInRound()
                && raiseCount < 3;
    }

    public void onPlayerRaise() {
        currentMinBetAmount = pricePool.raise(indexOfCurrentPlayer, round, RAISE_AMOUNT);
        playersLastAction[indexOfCurrentPlayer] = PlayerActionType.RAISE;
        IntStream.range(0, playerCount)
                .filter(i -> playersLastAction[i] != PlayerActionType.ALLIN
                        && playersLastAction[i] != PlayerActionType.FOLD)
                .filter(i -> !waitingPlayers.contains(i))
                .filter(i -> i != indexOfCurrentPlayer)
                .forEach(waitingPlayers::add);
        indexOfCurrentPlayer = waitingPlayers.poll();
        raiseCount++;
    }

    public boolean canPlayerPass() {
        return !waitingPlayers.isEmpty() && playersLastAction[indexOfCurrentPlayer] != PlayerActionType.PASS;
    }

    public void onPlayerPass() {
        waitingPlayers.add(indexOfCurrentPlayer);
        indexOfCurrentPlayer = waitingPlayers.poll();
        playersLastAction[indexOfCurrentPlayer] = PlayerActionType.PASS;
    }

    public void onPlayerAllIn() {
        int allInAmount = players[indexOfCurrentPlayer].getBalance();
        if (allInAmount >= currentMinBetAmount) {
            IntStream.range(0, playerCount)
                    .filter(i -> playersLastAction[i] != PlayerActionType.ALLIN
                            && playersLastAction[i] != PlayerActionType.FOLD)
                    .filter(i -> !waitingPlayers.contains(i))
                    .filter(i -> i != indexOfCurrentPlayer)
                    .forEach(waitingPlayers::add);
            currentMinBetAmount = allInAmount;
        }
        pricePool.allIn(indexOfCurrentPlayer, round, allInAmount);
        playersLastAction[indexOfCurrentPlayer] = PlayerActionType.ALLIN;
        indexOfCurrentPlayer = waitingPlayers.poll();
    }

    public void share(List<List<Integer>> winners) {
        Map<Integer, Integer> winnerMap = pricePool.winAmount(winners);
        winnerMap.forEach((k, v) -> {
            players[k].increaseBalance(v);
        });
    }

    public String toString() {
        return new StringBuilder().append("当前操作的玩家：").append(players[indexOfCurrentPlayer].getName()).append("\r\t")
                .append("最小下注金额：").append(currentMinBetAmount).append("\r\t")
                .append("玩家的操作：").append(playersLastAction[indexOfCurrentPlayer]).append("\r\t")
                .append("等待操作的玩家：").append(
                        waitingPlayers.stream()
                                .map(i -> {
                                    if (playersLastAction[i] == PlayerActionType.PASS) {
                                        return "[" + players[i].getName() + "]";
                                    } else {
                                        return players[i].getName();
                                    }
                                })
                                .collect(Collectors.joining(","))).append("\r\t")
                .append("退出的玩家：").append(
                        quitPlayers.stream()
                                .map(i -> players[i].getName())
                                .collect(Collectors.joining(","))).append("\r\t")
                .append("奖池：").append(Arrays.deepToString(pricePool.toArray(round))).append("\r\t")
                .toString();
    }

}
