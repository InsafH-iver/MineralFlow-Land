package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.Weighbridge;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class WeighbridgeRepositoryTest {

    @Autowired
    private WeighbridgeRepository weighbridgeRepository;

    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        postgreSQLContainer.start();
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    void findRandomWeighbridge() {
        //ARRANGE
        weighbridgeRepository.save(new Weighbridge(1));
        weighbridgeRepository.save(new Weighbridge(2));
        weighbridgeRepository.save(new Weighbridge(3));
        //ACT
        Optional<Weighbridge> optionalWeighbridge = weighbridgeRepository.findRandomWeighbridge();
        //ASSERT
        assertTrue(optionalWeighbridge.isPresent());
        assertThat(optionalWeighbridge.get().getWeighbridgeNumber()).isBetween(1, 3);
    }
}