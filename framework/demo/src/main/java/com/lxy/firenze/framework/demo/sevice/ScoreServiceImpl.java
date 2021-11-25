package com.lxy.firenze.framework.demo.sevice;

import com.lxy.firenze.framework.demo.dto.Score;
import com.lxy.firenze.framework.demo.repository.ScoreRepository;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class ScoreServiceImpl implements ScoreService {

    @Inject
    private ScoreRepository scoreRepository;

    @Override
    public Score create() {
        return scoreRepository.create();
    }

    @Override
    public Score get(Integer id) {
        return scoreRepository.get(id);
    }
}
