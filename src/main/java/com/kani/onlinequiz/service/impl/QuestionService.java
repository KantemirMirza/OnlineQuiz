package com.kani.onlinequiz.service.impl;

import com.kani.onlinequiz.entity.Question;
import com.kani.onlinequiz.repository.IQuestionRepository;
import com.kani.onlinequiz.service.IQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService implements IQuestionService {
    private final IQuestionRepository questionRepository;

    @Override
    public Question saveQuestionToDB(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public List<Question> getAllQuestionsFromDB() {
        return questionRepository.findAll();
    }

    @Override
    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    @Override
    public List<String> getAllSubjectsFromDB() {
        return questionRepository.findDistinctSubject();
    }

    @Override
    public Question updateQuestion(Long questionId, Question question) throws ChangeSetPersister.NotFoundException {
        Optional<Question> theQuestion = this.getQuestionById(questionId);
        if(theQuestion.isPresent()){
            Question updateQuestions = theQuestion.get();
            updateQuestions.setQuestion(question.getQuestion());
            updateQuestions.setQuestion(question.getChoices().toString());
            updateQuestions.setQuestion(question.getCorrectAnswers().toString());
            return questionRepository.save(updateQuestions);
        }else {
            throw  new ChangeSetPersister.NotFoundException();
        }
    }

    @Override
    public void deleteQuestionById(Long questionId) {
        questionRepository.deleteById(questionId);
    }

    @Override
    public List<Question> getQuestionsForUser(Integer numOfQuestions, String subject) {
        Pageable pageable = PageRequest.of(0, numOfQuestions);
        return questionRepository.findBySubject(subject, pageable).getContent();
    }
}
