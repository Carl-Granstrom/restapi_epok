package ltu.integration.restapi.tests;

import ltu.integration.restapi.model.Student;
import ltu.integration.restapi.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude= SecurityAutoConfiguration.class)
public class DBLoader {

    UUID stud4id;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    StudentRepository studentRepository;

    @Test
    public void testExampledata() {
        studentRepository.save(new Student("Carl", "Granstrom", "198309290313")).block();
        Student student2 = studentRepository.save(new Student("Hulk", "Hogan", "200712220417")).block();
        Student student3 = studentRepository.save(new Student("Hannah", "Montana", "200003047222")).block();
        Student student4 = studentRepository.save(new Student("Gurra", "Grankott", "200001010101")).block();
        stud4id = student4.getId();
    }

    @Test
    public void testGetSingleStudent() {

        webTestClient.get()
                .uri("/students/{id}", Collections.singletonMap("id", stud4id))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull());
    }

    @Test
    public void testGetAllStudents() {
        webTestClient.get().uri("/students")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Student.class);
    }


}
