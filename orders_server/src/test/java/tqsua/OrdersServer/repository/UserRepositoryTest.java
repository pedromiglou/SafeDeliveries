package tqsua.OrdersServer.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import tqsua.OrdersServer.model.User;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Test
    void whenGetAllUsers_thenReturnCorrectResults() throws IOException, InterruptedException {
        User user1 = new User("Pedro", "Amaral", "pedro@gmail.com", "1234", "U");
        User user2 = new User("Diogo", "Cunha", "cunha@gmail.com", "1234", "A");
        entityManager.persistAndFlush(user1);
        entityManager.persistAndFlush(user2);
        
        ArrayList<User> found = repository.findAll();
        assertThat(found.get(0)).isEqualTo(user1);
        assertThat(found.get(1)).isEqualTo(user2);
    }


}