package com.kani.onlinequiz.service;

import com.kani.onlinequiz.entity.Question;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;
import java.util.Optional;

public interface IQuestionService {
    Question saveQuestionToDB(Question question);

    List<Question> getAllQuestionsFromDB();

    Optional<Question> getQuestionById(Long id);

    List<String> getAllSubjectsFromDB();

    Question updateQuestion(Long questionId, Question question) throws ChangeSetPersister.NotFoundException;

    void deleteQuestionById(Long questionId);

    List<Question> getQuestionsForUser(Integer numOfQuestions, String subject);
}
