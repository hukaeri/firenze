package com.lxy.firenze.holdem.domain;

import com.lxy.firenze.holdem.constant.PlayerActionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Holdem {

    private static final int PUBLIC_CARD_COUNT = 5;
    private static final int PLAYER_CARD_COUNT = 2;
    private static final int MIN_BET_AMOUNT = 10;
    private static final int RAISE_AMOUNT = 10;

    private Integer playerCount;
    private Poker poker;
    private Player[] players;
    private Poker.Card[][] playerCards;
    private Poker.Card[] publicCards;
    private Round round;
    private int button;
    private PricePool pricePool;

    private Integer indexOfCurrentPlayer;
    private int currentMinBetAmount;
    private Queue<Integer> waitingPlayers;
    private List<Integer> quitPlayers;
    private PlayerActionType[] playersLastAction;
    private Map<Round, List<Integer>> allInMap;

    public Holdem(List<Player> players) {
        poker = new Poker();
        playerCount = players.size();
        this.players = players.toArray(new Player[playerCount]);
        playerCards = new Poker.Card[playerCount][PLAYER_CARD_COUNT];
        publicCards = new Poker.Card[PUBLIC_CARD_COUNT];
        round = Round.PRE_FLOP;
        button = playerCount - 1;
        pricePool = new PricePool(playerCount);
        pricePool.addPricePool(round);

        indexOfCurrentPlayer = 0;
        currentMinBetAmount = MIN_BET_AMOUNT;
        waitingPlayers = IntStream.range(1, playerCount).boxed().collect(Collectors.toCollection(LinkedList::new));
        quitPlayers = new ArrayList<>();
        playersLastAction = new PlayerActionType[playerCount];
        allInMap = new LinkedHashMap<>();
    }

    public boolean isGameOver() {
        return quitPlayers.size() == playerCount || round == null;
    }

    public boolean isRoundOver() {
        return indexOfCurrentPlayer == null || quitPlayers.size() == playerCount;
    }

    public Player currentActionPlayer() {
        return players[indexOfCurrentPlayer];
    }

    public int getIndexOfCurrentPlayer() {
        return indexOfCurrentPlayer;
    }

    public boolean isSmallBlindTurn() {
        return round == Round.PRE_FLOP && indexOfCurrentPlayer == 1;
    }

    public boolean isBigBlindTurn() {
        return round == Round.PRE_FLOP && indexOfCurrentPlayer == 2;
    }

    private void addAllInMap(Integer player) {
        if (allInMap.containsKey(round)) {
            allInMap.get(round).add(player);
        } else {
            List<Integer> raisePlayers = new ArrayList<>();
            raisePlayers.add(player);
            allInMap.put(round, raisePlayers);
        }
    }

    public Map<Round, List<Integer>> getAllInMapMap() {
        return allInMap;
    }

    public List<Integer> finalPlayers() {
        return IntStream.range(0, playerCount)
                .filter(this::isActivePlayer)
                .boxed()
                .collect(Collectors.toList());
    }

    public void nextRound() {
        clearPlayerBalance();

        round = round.next();
        if (round == null) {
            return;
        }

        indexOfCurrentPlayer = 0;
        while (!isActivePlayer(indexOfCurrentPlayer) && indexOfCurrentPlayer < playerCount) {
            indexOfCurrentPlayer++;
        }
        currentMinBetAmount = MIN_BET_AMOUNT;
        waitingPlayers.clear();
        makeOthersWait();
        IntStream.range(0, playerCount).filter(this::isActivePlayer).forEach(i -> playersLastAction[i] = null);
        pricePool.addPricePool(round);
    }

    private void clearPlayerBalance() {
        for (int i = 0; i < playerCount; i++) {
            players[i].decreaseAmount(pricePool.getPlayerAmountInARound(i, round));
        }
    }

    public int getMinBetAmount() {
        return currentMinBetAmount;
    }

    public int[][] getPools() {
        return pricePool.toArray();
    }

    public Round getRound() {
        return round;
    }

    public Poker getPoker() {
        return poker;
    }

    public Integer getPlayerCount() {
        return playerCount;
    }

    public Poker.Card[][] getPlayerCards() {
        return playerCards;
    }

    public Poker.Card[] getPublicCards() {
        return publicCards;
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

    private boolean isActivePlayer(int index) {
        return playersLastAction[index] != PlayerActionType.ALLIN
                && playersLastAction[index] != PlayerActionType.FOLD;
    }

    private void makeOthersWait() {
        IntStream.range(0, playerCount)
                .filter(this::isActivePlayer)
                .filter(i -> !waitingPlayers.contains(i))
                .filter(i -> i != indexOfCurrentPlayer)
                .forEach(waitingPlayers::add);
    }

    public void onPlayerBet() {
        pricePool.bet(indexOfCurrentPlayer, round, currentMinBetAmount, isFirstInRound());

        playersLastAction[indexOfCurrentPlayer] = PlayerActionType.BET;
        indexOfCurrentPlayer = waitingPlayers.poll();
    }

    public void onPlayerFold() {
        quitPlayers.add(indexOfCurrentPlayer);

        playersLastAction[indexOfCurrentPlayer] = PlayerActionType.FOLD;
        indexOfCurrentPlayer = waitingPlayers.poll();
    }

    public void onPlayerRaise() {
        currentMinBetAmount = pricePool.raise(indexOfCurrentPlayer, round, RAISE_AMOUNT);
        makeOthersWait();

        playersLastAction[indexOfCurrentPlayer] = PlayerActionType.RAISE;
        indexOfCurrentPlayer = waitingPlayers.poll();
    }

    public void onPlayerPass() {
        waitingPlayers.add(indexOfCurrentPlayer);

        playersLastAction[indexOfCurrentPlayer] = PlayerActionType.PASS;
        indexOfCurrentPlayer = waitingPlayers.poll();
    }

    public void onPlayerAllIn() {
        int allInAmount = players[indexOfCurrentPlayer].getBalance();
        if (allInAmount >= currentMinBetAmount) {
            makeOthersWait();
            currentMinBetAmount = allInAmount;
        }
        pricePool.allIn(indexOfCurrentPlayer, round, allInAmount);
        addAllInMap(indexOfCurrentPlayer);

        playersLastAction[indexOfCurrentPlayer] = PlayerActionType.ALLIN;
        indexOfCurrentPlayer = waitingPlayers.poll();
    }

    public boolean canPlayerRaise() {
        return currentActionPlayer().getBalance() >= currentMinBetAmount
                && !isFirstInRound();
    }

    public boolean canPlayerPass() {
        return !waitingPlayers.isEmpty() && playersLastAction[indexOfCurrentPlayer] != PlayerActionType.PASS;
    }

    public void share(List<List<Integer>> winners) {
        Map<Integer, Integer> winnerMap = pricePool.winAmount(winners);
        winnerMap.forEach((k, v) -> players[k].increaseBalance(v));
    }

    public String toString() {
        return new StringBuilder().append("当前操作的玩家：").append(players[indexOfCurrentPlayer].getName()).append("\r\n")
                .append("最小下注金额：").append(currentMinBetAmount).append("\r\n")
                .append("玩家的操作：").append(playersLastAction[indexOfCurrentPlayer]).append("\r\n")
                .append("等待操作的玩家：").append(
                        waitingPlayers.stream()
                                .map(i -> {
                                    if (playersLastAction[i] == PlayerActionType.PASS) {
                                        return "[" + players[i].getName() + "]";
                                    } else {
                                        return players[i].getName();
                                    }
                                })
                                .collect(Collectors.joining(","))).append("\r\n")
                .append("退出的玩家：").append(
                        quitPlayers.stream()
                                .map(i -> players[i].getName())
                                .collect(Collectors.joining(","))).append("\r\n")
                .append("奖池：").append(Arrays.deepToString(pricePool.toArray(round))).append("\r\n")
                .toString();
    }

}
