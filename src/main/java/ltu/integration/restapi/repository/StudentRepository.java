package ltu.integration.restapi.repository;

import ltu.integration.restapi.model.Student;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends ReactiveMongoRepository<Student, String> {

}
