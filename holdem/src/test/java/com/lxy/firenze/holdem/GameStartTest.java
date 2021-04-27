package com.lxy.firenze.holdem;

import com.lxy.firenze.holdem.domain.Holdem;
import com.lxy.firenze.holdem.domain.Player;
import com.lxy.firenze.holdem.exception.GameInitialException;
import com.lxy.firenze.holdem.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameStartTest extends BaseTest {

    @Autowired
    private GameService gameService;

    @Test
    public void should_not_start_game_when_player_count_less_than_4() {
        Player playerA = playerBuilder("A").build();

        assertThrows(GameInitialException.class, () -> gameService.start(Collections.singletonList(playerA)));
    }

    @Test
    public void should_not_start_game_when_player_count_more_than_10() {
        List<Player> players = IntStream.range(0, 11)
                .mapToObj(i -> playerBuilder(String.valueOf(i)).build())
                .collect(Collectors.toList());

        assertThrows(GameInitialException.class, () -> gameService.start(players));
    }

    @Test
    public void should_start_game_when_player_count_between_4_and_20() {
        Player playerA = playerBuilder("A").build();
        Player playerB = playerBuilder("B").build();
        Player playerC = playerBuilder("C").build();
        Player playerD = playerBuilder("D").build();

        Holdem holdem = gameService.start(Arrays.asList(playerA, playerB, playerC, playerD));

        assertThat(holdem).isNotNull();
    }
}
