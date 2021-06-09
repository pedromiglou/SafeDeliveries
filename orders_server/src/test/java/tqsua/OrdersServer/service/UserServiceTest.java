package tqsua.OrdersServer.service;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;

import java.util.ArrayList;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tqsua.OrdersServer.model.User;
import tqsua.OrdersServer.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    
    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @Test
    void whenGetAllUsers_thenReturnCorrectResults() throws Exception {
        ArrayList<User> response = new ArrayList<>();
        User user1 = new User("Pedro", "Amaral", "pedro@gmail.com", "1234", "U");
        User user2 = new User("Diogo", "Cunha", "cunha@gmail.com", "1234", "A");
        response.add(user1);
        response.add(user2);

        when(repository.findAll()).thenReturn(response);
        assertThat(service.getAllUsers()).isEqualTo(response);
        reset(repository);
    }


    @Test
    void whenSearchUserExistsByEmail_ifUserExists_ReturnTrue() {
        when(repository.existsUserByEmail(anyString())).thenReturn(true);

        //check if service returns true when a user with that email already exists
        service.existsUserByEmail(anyString());
        assertThat(service.existsUserByEmail(anyString())).isTrue();
    }

    @Test
    void whenSearchuserExistsByEmail_ifuserNotExists_ReturnFalse() {
        when(repository.existsUserByEmail(anyString())).thenReturn(false);

        //check if service returns false when a user with that email do not exists
        service.existsUserByEmail(anyString());
        assertThat(service.existsUserByEmail(anyString())).isFalse();
    }

}