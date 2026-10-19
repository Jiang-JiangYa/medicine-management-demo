package org.example.myspringboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable(named="RUN_DATABASE_TESTS", matches="true")
@SpringBootTest
class MySpringbootApplicationTests {

    @Test
    void contextLoads() {
    }

}
