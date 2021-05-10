package com.lxy.firenze.holdem.domain;

public enum Round {
    PRE_FLOP {
        @Override
        public void deal(Holdem holdem) {
            holdem.getPoker().shuffle();
            for (int i = 0; i < holdem.getPlayerCount(); i++) {
                holdem.getPlayerCards()[i][0] = holdem.getPoker().deal();
                holdem.getPlayerCards()[i][1] = holdem.getPoker().deal();
            }
        }
    },
    FLOP {
        @Override
        public void deal(Holdem holdem) {
            holdem.getPoker().cut();
            for (int i = 0; i < 3; i++) {
                holdem.getPublicCards()[i] = holdem.getPoker().deal();
            }
        }
    },
    TURN {
        @Override
        public void deal(Holdem holdem) {
            holdem.getPoker().cut();
            holdem.getPublicCards()[3] = holdem.getPoker().deal();
        }
    },
    RIVER {
        @Override
        public void deal(Holdem holdem) {
            holdem.getPublicCards()[4] = holdem.getPoker().deal();
        }
    };

    public abstract void deal(Holdem holdem);

    public Round next() {
        return this == RIVER ? null : Round.values()[this.ordinal() + 1];
    }
}
