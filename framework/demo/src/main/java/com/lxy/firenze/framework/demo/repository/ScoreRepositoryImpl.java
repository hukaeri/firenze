package com.lxy.firenze.framework.demo.repository;

import com.lxy.firenze.framework.demo.dto.Score;

import javax.inject.Named;

@Named
public class ScoreRepositoryImpl implements ScoreRepository {

    @Override
    public Score create() {
        return Score.create();
    }

    @Override
    public Score get(Integer id) {
        return Score.all().get(id);
    }
}
