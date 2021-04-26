package com.lxy.firenze.holdem.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PricePool {

    private List<InnerPool> pools = new ArrayList<>();

    private int playerCount;

    public PricePool(int playerCount) {
        this.playerCount = playerCount;
    }

    private static class InnerPool {
        private int round;

        private int[] pool;

        private InnerPool(int round, int playerCount) {
            this.round = round;
            this.pool = new int[playerCount];
        }

        private InnerPool(int round, int[] pool) {
            this.round = round;
            this.pool = Arrays.copyOf(pool, pool.length);
        }

        private int max() {
            return Arrays.stream(pool).max().getAsInt();
        }

        private int sum() {
            return Arrays.stream(pool).sum();
        }

        private boolean winnerInPool(List<Integer> winners) {
            return winners.stream().allMatch(w -> pool[w] > 0);
        }

        private void reset(int amount) {
            for (int i = 0; i < pool.length; i++) {
                if (pool[i] != 0) {
                    pool[i] = amount;
                }
            }
        }
    }

    public void addPricePool(int round) {
        pools.add(new InnerPool(round, playerCount));
    }


    public int getPlayerAmountInARound(int playerIndex, int round) {
        return pools.stream()
                .filter(p -> p.round == round)
                .mapToInt(p -> p.pool[playerIndex])
                .sum();
    }

    public void bet(int player, int round, int amount, boolean isFirst) {
        if (isFirst) {
            pools.stream().filter(p -> p.round == round)
                    .findFirst()
                    .ifPresent(p -> p.pool[player] = amount);
        } else {
            pools.stream().filter(p -> p.round == round)
                    .forEach(p -> p.pool[player] = p.max());
        }
    }

    public int raise(int player, int round, int raiseAmount) {
        int amount = 0;
        for (int i = 0; i < pools.size(); i++) {
            InnerPool p = pools.get(i);
            if (p.round == round) {
                if (i == pools.size() - 1 || pools.get(i + 1).round > round) {
                    p.pool[player] = p.max() + raiseAmount;
                } else {
                    p.pool[player] = p.max();
                }
                amount += p.pool[player];
            } else {
                break;
            }
        }
        return amount;
    }

    public void allIn(int player, int round, int amount) {
        int already = 0;
        for (int i = 0; i < pools.size(); i++) {
            InnerPool p = pools.get(i);
            if (p.round == round) {
                if (i == pools.size() - 1 || pools.get(i + 1).round > round) {
                    p.pool[player] = amount - already;
                } else {
                    p.pool[player] = p.max();
                }
                already += p.pool[player];
            } else {
                break;
            }
        }
    }

    public void separate(int amount, int round, int currentMinBetAmount) {
        int separateAmount = currentMinBetAmount - amount;
        for (int i = 0; i < pools.size(); i++) {
            InnerPool p = pools.get(i);
            if (p.round == round) {
                if (i == pools.size() - 1 || pools.get(i + 1).round > round) {
                    p.reset(p.max() - separateAmount);
                    InnerPool separatePool = new InnerPool(round, p.pool);
                    separatePool.reset(separateAmount);
                    pools.add(separatePool);
                    break;
                }
            }
        }
    }

    public Map<Integer, Integer> winAmount(List<List<Integer>> winners) {
        Map<Integer, Integer> winnerMap = new HashMap<>();
        for (InnerPool p : pools) {
            for (List<Integer> ws : winners) {
                if (p.winnerInPool(ws)) {
                    addAmountForWinner(winnerMap, ws, p.sum());
                    break;
                }

            }
        }
        return winnerMap;
    }

    private void addAmountForWinner(Map<Integer, Integer> map, List<Integer> players, int amount) {
        int average = amount / players.size();
        for (Integer player : players) {
            if (map.containsKey(player)) {
                map.put(player, map.get(player) + average);
            } else {
                map.put(player, average);
            }
        }
    }

    public int[][] toArray() {
        int[][] array = new int[pools.size()][];
        for (int i = 0; i < pools.size(); i++) {
            array[i] = pools.get(i).pool;
        }
        return array;
    }

    public int[][] toArray(int round) {
        return pools.stream().filter(p -> p.round == round)
                .map(p -> p.pool)
                .toArray(int[][]::new);
    }

}
