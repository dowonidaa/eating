package com.project.eat.review.word;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BadWordService {


    @Autowired
    private BadWordRepository badWordRepository;


    public List<BadwordVO> badwordVOList(){
        return badWordRepository.findAll();
    }

}
