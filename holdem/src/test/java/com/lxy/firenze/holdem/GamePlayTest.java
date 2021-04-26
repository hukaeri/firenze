package com.lxy.firenze.holdem;

import com.lxy.firenze.holdem.domain.Holdem;
import com.lxy.firenze.holdem.domain.Player;
import com.lxy.firenze.holdem.domain.PlayerAction;
import com.lxy.firenze.holdem.exception.UnsupportActionException;
import com.lxy.firenze.holdem.service.GameService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class GamePlayTest extends BaseTest {

    @Autowired
    private GameService gameService;

    @Test
    public void should_not_bet_when_player_do_not_have_enough_money() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").balance(5).build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(eq(playerA), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerB), anyInt())).thenReturn(new PlayerAction.Bet());

        assertThrows(UnsupportActionException.class, () -> gameService.play(holdem));
    }

    @Test
    public void should_game_over_when_all_players_bet() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(any(Player.class), anyInt())).thenReturn(new PlayerAction.Bet());

        gameService.play(holdem);

        assertThat(holdem.isGameOver()).isTrue();
        int[][] pricePool = {
                {10, 10, 10, 10},
                {10, 10, 10, 10},
                {10, 10, 10, 10},
                {10, 10, 10, 10}
        };
        assertThat(holdem.getPools()).isEqualTo(pricePool);
    }

    @Test
    public void should_not_raise_when_player_do_not_have_enough_money() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").balance(5).build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(eq(playerA), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerB), anyInt())).thenReturn(new PlayerAction.Raise());

        assertThrows(UnsupportActionException.class, () -> gameService.play(holdem));
    }

    @Test
    public void should_not_raise_when_player_is_first_in_a_round() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(eq(playerA), anyInt())).thenReturn(new PlayerAction.Raise());

        assertThrows(UnsupportActionException.class, () -> gameService.play(holdem));
    }

    @Test
    public void should_not_raise_when_already_raise_3_times_in_a_round() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(eq(playerA), anyInt())).thenReturn(new PlayerAction.Bet())
                .thenReturn(new PlayerAction.Raise());
        when(askPlayerAction.ask(eq(playerB), anyInt())).thenReturn(new PlayerAction.Raise());
        when(askPlayerAction.ask(eq(playerC), anyInt())).thenReturn(new PlayerAction.Raise());
        when(askPlayerAction.ask(eq(playerD), anyInt())).thenReturn(new PlayerAction.Raise());

        assertThrows(UnsupportActionException.class, () -> gameService.play(holdem));
    }

    @Test
    public void should_game_over_when_one_player_raise_in_a_round() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(eq(playerA), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerB), anyInt())).thenReturn(new PlayerAction.Raise())
                .thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerC), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerD), anyInt())).thenReturn(new PlayerAction.Bet());

        gameService.play(holdem);

        assertThat(holdem.isGameOver()).isTrue();
        int[][] pricePool = {
                {20, 20, 20, 20},
                {10, 10, 10, 10},
                {10, 10, 10, 10},
                {10, 10, 10, 10}
        };
        assertThat(holdem.getPools()).isEqualTo(pricePool);
    }

    @Test
    public void should_game_over_when_two_player_raise_in_a_round() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(eq(playerA), anyInt())).thenReturn(new PlayerAction.Bet())
                .thenReturn(new PlayerAction.Raise())
                .thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerB), anyInt())).thenReturn(new PlayerAction.Raise())
                .thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerC), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerD), anyInt())).thenReturn(new PlayerAction.Bet());

        gameService.play(holdem);

        assertThat(holdem.isGameOver()).isTrue();
        int[][] pricePool = {
                {30, 30, 30, 30},
                {10, 10, 10, 10},
                {10, 10, 10, 10},
                {10, 10, 10, 10}
        };
        assertThat(holdem.getPools()).isEqualTo(pricePool);
    }

    @Test
    public void should_loose_amount_after_game_when_one_player_fold_in_a_round() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(eq(playerA), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerB), anyInt())).thenReturn(new PlayerAction.Bet())
                .thenReturn(new PlayerAction.Fold());
        when(askPlayerAction.ask(eq(playerC), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerD), anyInt())).thenReturn(new PlayerAction.Bet());

        gameService.play(holdem);

        assertThat(holdem.isGameOver()).isTrue();
        int[][] pricePool = {
                {10, 10, 10, 10},
                {10, 0, 10, 10},
                {10, 0, 10, 10},
                {10, 0, 10, 10}
        };
        assertThat(holdem.getPools()).isEqualTo(pricePool);
        assertThat(playerB.getBalance()).isEqualTo(90);
    }

    @Test
    public void should_not_pass_when_player_is_last_in_a_round() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(eq(playerA), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerB), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerC), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerD), anyInt())).thenReturn(new PlayerAction.Pass());

        assertThrows(UnsupportActionException.class, () -> gameService.play(holdem));
    }

    @Test
    public void should_not_pass_when_player_just_pass_in_a_round() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(eq(playerA), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerB), anyInt())).thenReturn(new PlayerAction.Raise());
        when(askPlayerAction.ask(eq(playerC), anyInt())).thenReturn(new PlayerAction.Pass());
        when(askPlayerAction.ask(eq(playerD), anyInt())).thenReturn(new PlayerAction.Pass());

        assertThrows(UnsupportActionException.class, () -> gameService.play(holdem));
    }

    @Test
    public void should_game_over_when_one_player_pass_in_a_round() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(eq(playerA), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerB), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerC), anyInt())).thenReturn(new PlayerAction.Pass())
                .thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerD), anyInt())).thenReturn(new PlayerAction.Bet());

        gameService.play(holdem);

        assertThat(holdem.isGameOver()).isTrue();
        int[][] pricePool = {
                {10, 10, 10, 10},
                {10, 10, 10, 10},
                {10, 10, 10, 10},
                {10, 10, 10, 10}
        };
        assertThat(holdem.getPools()).isEqualTo(pricePool);
    }

    @Test
    public void should_game_over_when_some_player_pass_and_raise_in_a_round() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(eq(playerA), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerB), anyInt())).thenReturn(new PlayerAction.Pass())
                .thenReturn(new PlayerAction.Raise())
                .thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerC), anyInt())).thenReturn(new PlayerAction.Raise())
                .thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerD), anyInt())).thenReturn(new PlayerAction.Pass())
                .thenReturn(new PlayerAction.Bet());

        gameService.play(holdem);

        assertThat(holdem.isGameOver()).isTrue();
        int[][] pricePool = {
                {30, 30, 30, 30},
                {10, 10, 10, 10},
                {10, 10, 10, 10},
                {10, 10, 10, 10}
        };
        assertThat(holdem.getPools()).isEqualTo(pricePool);
    }

    @Test
    public void should_price_pool_separate_when_a_player_all_in_and_amount_less_than_min_bet_amount() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").balance(5).build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(eq(playerA), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerB), anyInt())).thenReturn(new PlayerAction.AllIn());
        when(askPlayerAction.ask(eq(playerC), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerD), anyInt())).thenReturn(new PlayerAction.Bet());

        gameService.play(holdem);

        assertThat(holdem.isGameOver()).isTrue();
        int[][] pricePool = {
                {5, 5, 5, 5}, // round1
                {5, 0, 5, 5}, // round1
                {10, 0, 10, 10},
                {10, 0, 10, 10},
                {10, 0, 10, 10}
        };
        assertThat(holdem.getPools()).isEqualTo(pricePool);
    }

    @Test
    public void should_not_price_pool_separate_when_a_player_all_in_and_amount_more_than_min_bet_amount() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").balance(15).build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(eq(playerA), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerB), anyInt())).thenReturn(new PlayerAction.AllIn());
        when(askPlayerAction.ask(eq(playerC), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerD), anyInt())).thenReturn(new PlayerAction.Bet());

        gameService.play(holdem);

        assertThat(holdem.isGameOver()).isTrue();
        int[][] pricePool = {
                {15, 15, 15, 15},
                {10, 0, 10, 10},
                {10, 0, 10, 10},
                {10, 0, 10, 10}
        };
        assertThat(holdem.getPools()).isEqualTo(pricePool);
    }

    @Test
    public void should_price_pool_separate_when_two_player_all_in_and_amount_less_than_min_bet_amount() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").balance(5).build();
        Player playerC = playerBuilder("C").balance(8).build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(eq(playerA), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerB), anyInt())).thenReturn(new PlayerAction.AllIn());
        when(askPlayerAction.ask(eq(playerC), anyInt())).thenReturn(new PlayerAction.AllIn());
        when(askPlayerAction.ask(eq(playerD), anyInt())).thenReturn(new PlayerAction.Bet());

        gameService.play(holdem);

        assertThat(holdem.isGameOver()).isTrue();
        int[][] pricePool = {
                {5, 5, 5, 5}, // round1
                {3, 0, 3, 3}, // round1
                {2, 0, 0, 2}, // round1
                {10, 0, 0, 10},
                {10, 0, 0, 10},
                {10, 0, 0, 10}
        };
        assertThat(holdem.getPools()).isEqualTo(pricePool);
    }

    @Test
    public void should_get_all_money_when_only_one_winner_and_price_pool_not_separated() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(any(Player.class), anyInt())).thenReturn(new PlayerAction.Bet());
        when(spreadService.winners(holdem)).thenReturn(Collections.singletonList(Collections.singletonList(0)));

        gameService.play(holdem);

        assertThat(holdem.isGameOver()).isTrue();
        int[][] pricePool = {
                {10, 10, 10, 10},
                {10, 10, 10, 10},
                {10, 10, 10, 10},
                {10, 10, 10, 10}
        };
        assertThat(holdem.getPools()).isEqualTo(pricePool);
        assertThat(playerA.getBalance()).isEqualTo(220);
        assertThat(playerB.getBalance()).isEqualTo(60);
        assertThat(playerC.getBalance()).isEqualTo(60);
        assertThat(playerD.getBalance()).isEqualTo(60);
    }

    @Test
    public void should_separate_money_equally_when_two_winners_and_price_pool_not_separated() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(any(Player.class), anyInt())).thenReturn(new PlayerAction.Bet());
        when(spreadService.winners(holdem))
                .thenReturn(Arrays.asList(Arrays.asList(0, 1)));

        gameService.play(holdem);

        assertThat(holdem.isGameOver()).isTrue();
        int[][] pricePool = {
                {10, 10, 10, 10},
                {10, 10, 10, 10},
                {10, 10, 10, 10},
                {10, 10, 10, 10}
        };
        assertThat(holdem.getPools()).isEqualTo(pricePool);
        assertThat(playerA.getBalance()).isEqualTo(140);
        assertThat(playerB.getBalance()).isEqualTo(140);
        assertThat(playerC.getBalance()).isEqualTo(60);
        assertThat(playerD.getBalance()).isEqualTo(60);
    }

    @Test
    public void should_get_all_money_when_only_one_winner_and_other_player_all_in() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").balance(5).build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(eq(playerA), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerB), anyInt())).thenReturn(new PlayerAction.AllIn());
        when(askPlayerAction.ask(eq(playerC), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerD), anyInt())).thenReturn(new PlayerAction.Bet());
        when(spreadService.winners(holdem)).thenReturn(Collections.singletonList(Collections.singletonList(0)));

        gameService.play(holdem);

        assertThat(holdem.isGameOver()).isTrue();
        int[][] pricePool = {
                {5, 5, 5, 5}, // round1
                {5, 0, 5, 5}, // round1
                {10, 0, 10, 10},
                {10, 0, 10, 10},
                {10, 0, 10, 10}
        };
        assertThat(holdem.getPools()).isEqualTo(pricePool);
        assertThat(playerA.getBalance()).isEqualTo(185);
        assertThat(playerB.getBalance()).isEqualTo(0);
        assertThat(playerC.getBalance()).isEqualTo(60);
        assertThat(playerD.getBalance()).isEqualTo(60);
    }

    @Test
    public void should_others_contest_base_on_rest_amount_when_a_all_in_winner() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").balance(5).build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();
        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));
        when(askPlayerAction.ask(eq(playerA), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerB), anyInt())).thenReturn(new PlayerAction.AllIn());
        when(askPlayerAction.ask(eq(playerC), anyInt())).thenReturn(new PlayerAction.Bet());
        when(askPlayerAction.ask(eq(playerD), anyInt())).thenReturn(new PlayerAction.Bet());
        when(spreadService.winners(holdem))
                .thenReturn(Arrays.asList(Collections.singletonList(1), Collections.singletonList(0)));

        gameService.play(holdem);

        assertThat(holdem.isGameOver()).isTrue();
        int[][] pricePool = {
                {5, 5, 5, 5}, // round1
                {5, 0, 5, 5}, // round1
                {10, 0, 10, 10},
                {10, 0, 10, 10},
                {10, 0, 10, 10}
        };
        assertThat(holdem.getPools()).isEqualTo(pricePool);
        assertThat(playerA.getBalance()).isEqualTo(165);
        assertThat(playerB.getBalance()).isEqualTo(20);
        assertThat(playerC.getBalance()).isEqualTo(60);
        assertThat(playerD.getBalance()).isEqualTo(60);
    }
}
