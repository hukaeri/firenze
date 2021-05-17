package com.lxy.firenze.holdem;

import com.lxy.firenze.holdem.domain.Holdem;
import com.lxy.firenze.holdem.domain.Player;
import com.lxy.firenze.holdem.domain.PlayerAction;
import com.lxy.firenze.holdem.service.BonusService;
import com.lxy.firenze.holdem.service.SpreadService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Demo {
    private static Map<String, PlayerAction> actionMap;

    static {
        actionMap = new HashMap<>();
        actionMap.put("bet", new PlayerAction.Bet());
        actionMap.put("raise", new PlayerAction.Raise());
        actionMap.put("fold", new PlayerAction.Fold());
        actionMap.put("pass", new PlayerAction.Pass());
        actionMap.put("allin", new PlayerAction.AllIn());
    }

    public static void main(String[] args) {
        System.out.println("======游戏开始=====");
        Scanner scanner = new Scanner(System.in);
        List<Player> players = new ArrayList<>();
        String name;
        while (true) {
            System.out.print("加入玩家/q-停止：");
            name = scanner.next();
            if (name.equals("q")) {
                break;
            }
            System.out.print("初始筹码：");
            int balance = scanner.nextInt();
            players.add(new Player(name, balance));
        }
        Holdem holdem = new Holdem(players);

        while (!holdem.isGameOver()) {
            holdem.getRound().deal(holdem);
            System.out.println(String.format("===Round:%s===", holdem.getRound().ordinal() + 1));
            while (!holdem.isRoundOver()) {
                Player player = holdem.currentActionPlayer();
                System.out.print(String.format("玩家【%s】您的手牌（%s），公共牌（%s），请操作：", player.getName(),
                        Arrays.toString(holdem.getPlayerCards()[holdem.getIndexOfCurrentPlayer()]),
                        Arrays.stream(holdem.getPublicCards())
                                .filter(c -> c != null)
                                .map(c -> c.toString())
                                .collect(Collectors.joining(",")))
                );
                PlayerAction action = actionMap.get(scanner.next());
                action.action(holdem);
            }
            holdem.nextRound();
        }

        System.out.println("==================");
        List<List<Integer>> winners = new SpreadService().winners(holdem);
        String winnerNames = winners.stream()
                .map(l -> String.format("[%s]", l.stream().map(i -> players.get(i).getName()).collect(Collectors.joining(","))))
                .collect(Collectors.joining(","));
        System.out.println("获胜的玩家：" + winnerNames);
        new BonusService().share(holdem, winners);
        System.out.println("奖金池：");
        System.out.println(Arrays.deepToString(holdem.getPools()));
        System.out.println("玩家剩余筹码：");
        players.stream().forEach(System.out::println);

    }
}
