package com.lxy.firenze.holdem;

import com.lxy.firenze.holdem.domain.Player;
import com.lxy.firenze.holdem.service.AskPlayerAction;
import com.lxy.firenze.holdem.service.SpreadService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
public abstract class BaseTest {

    @MockBean
    protected AskPlayerAction askPlayerAction;

    @MockBean
    protected SpreadService spreadService;

    protected Player.PlayerBuilder playerBuilder(String name) {
        return Player.builder().name(name).balance(100);
    }
}
