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
public class RestapiApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    StudentRepository studentRepository;

    @Test
    public void testCreateStudent() {
        Student student = new Student("Carl", "Granstrom", "198309290313");

        webTestClient.post().uri("/students")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(student), Student.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.firstName").isEqualTo("Carl")
                .jsonPath("$.lastName").isEqualTo("Granstrom");
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

    @Test
    public void testGetSingleStudent() {
        Student student = Student.builder().id(UUID.randomUUID()).firstName("Hulk").lastName("Hogan").personnummer("200712220417").build();
        studentRepository.save(student).block();
        webTestClient.get()
                .uri("/students/{id}", Collections.singletonMap("id", student.getId()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response ->
                        Assertions.assertThat(response.getResponseBody()).isNotNull());
    }

    @Test
    public void testUpdateStudent() {
        Student student = studentRepository.save(new Student("Hannah", "Montana", "200003047222")).block();
        Student newStudentData = new Student("Hannah", "Manyana", "200003047222");

        webTestClient.put()
                .uri("/students/{id}", Collections.singletonMap("id", student.getId()))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(newStudentData), Student.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.firstName").isEqualTo("Hannah")
                .jsonPath("$.lastName").isEqualTo("Manyana")
                .jsonPath("$.personnummer").isEqualTo("200003047222");
    }

    @Test
    public void testDeleteStudent() {
        Student student = studentRepository.save(new Student("Gurra", "Grankott", "200001010101")).block();
        System.out.println(student.getId());
        webTestClient.delete()
                .uri("/students/{id}", Collections.singletonMap("id",  student.getId()))
                .exchange()
                .expectStatus().isOk();
    }
}
