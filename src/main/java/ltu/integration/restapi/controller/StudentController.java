package ltu.integration.restapi.controller;

import ltu.integration.restapi.model.Student;
import ltu.integration.restapi.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/students")
    public Flux<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    @PostMapping("/students")
    public Mono<Student> createStudent(@Valid @RequestBody Student student){
        return studentRepository.save(student);
    }

    @GetMapping("/students/{id}")
    public Mono<ResponseEntity<Student>> getStudentById(@PathVariable(value = "id") String studentId){
        return studentRepository.findById(studentId)
                .map(savedStudent -> ResponseEntity.ok(savedStudent))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/students/{id}")
    public Mono<ResponseEntity<Void>> deleteStudent(@PathVariable(value = "id") String studentId){
        return studentRepository.findById(studentId)
                .flatMap(existingStudent ->
                        studentRepository.delete(existingStudent)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //todo not used
    // Created students are sent to the client as Server Sent Events
    @GetMapping(value = "/stream/tweets", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Student> streamAllStudents() {
        return studentRepository.findAll();
    }
}
