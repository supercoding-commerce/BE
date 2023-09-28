package com.github.commerce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
@SpringBootTest
class CommerceApplicationTests {

    @Test
    void contextLoads() {
    }

}
