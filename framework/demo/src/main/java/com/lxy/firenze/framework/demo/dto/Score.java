package com.lxy.firenze.framework.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class Score {

    private static List<Score> scores = new ArrayList<>();

    private String value;

    public Score(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("[%s]value: %s", this.getClass().getSimpleName(), value);
    }

    public static List<Score> all() {
        return scores;
    }

    public static Score create() {
        Score company = new Score(Score.class.getSimpleName() + "-" + scores.size());
        scores.add(company);
        return company;
    }

    public Score() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
