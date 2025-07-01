package com.jacaranda.manuel.VotaVision.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.jacaranda.manuel.VotaVision.model.Question;
import com.jacaranda.manuel.VotaVision.model.QuestionType;

@Component
public class QuestionDtoConverter {

    public QuestionDto convertQuestionToDto(Question question) {
        List<OptionDto> options = Collections.emptyList();

        if (question.getType() == QuestionType.MULTIPLE_CHOICE && question.getOptions() != null) {
            options = question.getOptions().stream()
                    .map(opt -> new OptionDto(opt.getId(), opt.getText()))
                    .collect(Collectors.toList());
        }

        return new QuestionDto(
                question.getId(),
                question.getText(),
                question.getType(),
                Boolean.TRUE.equals(question.getIsMandatory()),
                options
        );
    }
}
