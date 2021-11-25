package com.lxy.firenze.framework.demo.repository;

import com.lxy.firenze.framework.demo.dto.Score;

public interface ScoreRepository {

    Score create();

    Score get(Integer id);
}
