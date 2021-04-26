package com.lxy.firenze.holdem.domain;

import com.lxy.firenze.holdem.constant.PlayerActionType;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;
import java.util.function.Predicate;

@Getter
@Setter
public abstract class PlayerAction {

    private Consumer<Holdem> command;
    private Predicate<Holdem> validateCommand;

    public void action(Holdem holdem) {
        command.accept(holdem);
    }

    public boolean validate(Holdem holdem) {
        return validateCommand.test(holdem);
    }

    protected abstract PlayerActionType actionType();

    public static class Bet extends PlayerAction {

        public Bet() {
            setCommand(Holdem::onPlayerBet);
            setValidateCommand(Holdem::canPlayerBet);
        }

        @Override
        protected PlayerActionType actionType() {
            return PlayerActionType.BET;
        }
    }

    public static class Raise extends PlayerAction {

        public Raise() {
            setCommand(Holdem::onPlayerRaise);
            setValidateCommand(Holdem::canPlayerRaise);
        }

        @Override
        protected PlayerActionType actionType() {
            return PlayerActionType.RAISE;
        }
    }

    public static class Fold extends PlayerAction {

        public Fold() {
            setCommand(Holdem::onPlayerFold);
            setValidateCommand(holdem -> true);
        }

        @Override
        protected PlayerActionType actionType() {
            return PlayerActionType.FOLD;
        }
    }

    public static class Pass extends PlayerAction {

        public Pass() {
            setCommand(Holdem::onPlayerPass);
            setValidateCommand(Holdem::canPlayerPass);
        }

        @Override
        protected PlayerActionType actionType() {
            return PlayerActionType.PASS;
        }
    }

    public static class AllIn extends PlayerAction {

        public AllIn() {
            setCommand(Holdem::onPlayerAllIn);
            setValidateCommand(holdem -> true);
        }

        @Override
        protected PlayerActionType actionType() {
            return PlayerActionType.ALLIN;
        }
    }

}
