package com.jacaranda.manuel.VotaVision.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jacaranda.manuel.VotaVision.dto.CategoryDetailsDto.SurveySummaryDto;
import com.jacaranda.manuel.VotaVision.model.Question;
import com.jacaranda.manuel.VotaVision.model.QuestionType;
import com.jacaranda.manuel.VotaVision.model.Survey;
import com.jacaranda.manuel.VotaVision.model.User;
import com.jacaranda.manuel.VotaVision.repository.ParticipationRepository;

@Component
public class SurveyDtoConverter {

    @Autowired
    private QuestionDtoConverter questionDtoConverter;

    @Autowired
    private ParticipationRepository participationRepository;

    public SurveyDto convertSurveyDto(Survey survey, String userEmail) {
        User creator = survey.getCreator();

        CreatorSummaryDto creatorDto = new CreatorSummaryDto(creator.getId(), creator.getName(), creator.getEmail());

        CategorySummaryDto categoryDto = null;
        if (survey.getCategory() != null) {
            categoryDto = new CategorySummaryDto(survey.getCategory().getId(), survey.getCategory().getName(), survey.getCategory().getDescription());
        }

        SurveyDto dto = new SurveyDto(survey.getId(), survey.getTitle(), survey.getDescription(), survey.getStartDate(),
                survey.getCloseDate(), survey.getReward(), creatorDto);

        dto.setCategory(categoryDto);

        int totalResponses = survey.getParticipations() != null ? survey.getParticipations().size() : 0;
        dto.setResponsesCount(totalResponses);

        List<QuestionDto> questionDtos = survey.getQuestions().stream()
                .map(questionDtoConverter::convertQuestionToDto)
                .collect(Collectors.toList());
        dto.setQuestions(questionDtos);

        boolean hasParticipated = participationRepository.existsBySurveyIdAndUserEmail(survey.getId(), userEmail);
        dto.setCanRespond(!hasParticipated);

        return dto;
    }

    
    public SurveySummaryDto convertSurveySummaryDto(Survey survey) {
    	
    	SurveySummaryDto dto = new SurveySummaryDto(
                survey.getTitle(),
                survey.getStartDate(),
                survey.getCloseDate()
            );
    	
        return dto;
    }

    
    

    public QuestionDto convertQuestion(Question q) {
        List<OptionDto> options = null;

        if (q.getType() == QuestionType.MULTIPLE_CHOICE && q.getOptions() != null) {
            options = q.getOptions().stream().map(opt -> new OptionDto(opt.getId(), opt.getText()))
                    .collect(Collectors.toList());
        }

        return new QuestionDto(q.getId(), q.getText(), q.getType(), Boolean.TRUE.equals(q.getIsMandatory()), options);
    }
}