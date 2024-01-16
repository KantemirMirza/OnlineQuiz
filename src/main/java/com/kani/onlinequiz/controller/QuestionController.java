package com.kani.onlinequiz.controller;

import com.kani.onlinequiz.entity.Question;
import com.kani.onlinequiz.service.IQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static jakarta.mail.event.FolderEvent.CREATED;

@RestController
@RequestMapping("/api/quiz")
@RequiredArgsConstructor
public class QuestionController {
    private final IQuestionService questionService;

    @PostMapping("/create")
    public ResponseEntity<Question> createQuestion(@Valid @RequestBody Question question){
        Question questionCreated = questionService.saveQuestionToDB(question);
        return ResponseEntity.status(CREATED).body(questionCreated);
    }

    @GetMapping("/allQuestions")
    public ResponseEntity<List<Question>> getAllQuestions(){
        List<Question> questions = questionService.getAllQuestionsFromDB();
        return ResponseEntity.ok(questions);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable("id") Long questionId) throws ChangeSetPersister.NotFoundException {
        Optional<Question> findById = questionService.getQuestionById(questionId);
        if(findById.isPresent()){
            return ResponseEntity.ok(findById.get());
        }else{
            throw new ChangeSetPersister.NotFoundException();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable("id")Long questionId,
                                                   @RequestBody Question question) throws ChangeSetPersister.NotFoundException {
        Question update = questionService.updateQuestion(questionId, question);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable("id")Long questionId){
        questionService.deleteQuestionById(questionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/allSubjects")
    public ResponseEntity<List<String>> getAllSubjects(){
        List<String> subjects = questionService.getAllSubjectsFromDB();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/fetch-questions-for-user")
    public ResponseEntity<List<Question>> getQuestionForUser(@RequestParam Integer numOfQuestion,
                                                             @RequestParam String subject){
        List<Question> allQuestions = questionService.getQuestionsForUser(numOfQuestion, subject);
        List<Question> mutableQuestion = new ArrayList<>(allQuestions);
        Collections.shuffle(mutableQuestion);
        int availableQuestions = Math.min(numOfQuestion, mutableQuestion.size());
        List<Question> randomQuestion = mutableQuestion.subList(0, availableQuestions);
        return ResponseEntity.ok(randomQuestion);
    }
}
