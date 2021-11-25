package com.lxy.firenze.framework.demo.sevice;

import com.lxy.firenze.framework.demo.dto.Score;

public interface ScoreService {

    Score create();

    Score get(Integer id);
}
