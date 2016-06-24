package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;


interface PersonService {

    Person save(Person person);

    List<Person> findAll();
}


interface PersonRepository extends JpaRepository<Person, Long> {

}

@Repository
@Transactional(readOnly = true)
class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;

    @Autowired
    public PersonServiceImpl(PersonRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Person save(Person person) {
        return repository.save(person);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> findAll() {
        return repository.findAll();
    }
}

@SpringBootApplication
public class Demo1Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo1Application.class, args);
    }
}

@Component
class CLR implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(CLR.class);
    private final PersonService personService;

    @Autowired
    public CLR(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public void run(String... strings) throws Exception {
        personService.save(new Person("Test1"));
        personService.save(new Person("Test2"));
        personService.save(new Person("Test3"));
        personService.save(new Person("Test4"));
        personService.save(new Person("Test5"));
        personService.save(new Person("Test6"));
        personService.save(new Person("Test7"));

        log.info("Created 7 Test");
    }
}

@RestController
class GreetingController {

    private final PersonService personService;

    @Autowired
    public GreetingController(PersonService personService) {
        this.personService = personService;
    }

    @RequestMapping("/test/{name}")
    public void createTest(@PathVariable("name") String name) {
        Person person = new Person(name);
        personService.save(person);
    }

    @RequestMapping("/test")
    public List<Person> findAll() {
        return personService.findAll();
    }
}

@Entity
class Person {

    @Id
    @GeneratedValue
    private Long id;
    private String firstName;

    Person() {
        // why JPA why??
    }

    public Person(String firstName) {
        this.firstName = firstName;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }
}