package tqsua.DeliveriesServer.service;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import tqsua.DeliveriesServer.model.Notification;
import tqsua.DeliveriesServer.repository.NotificationRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    
    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private NotificationService service;

    @Test
    void whenGetNotificationByRider_thenReturnCorrectResults() throws Exception {
        Notification notification1 = new Notification(1, 2);

        when(repository.getByRiderId(1)).thenReturn(notification1);
        assertThat(service.getNotificationByRider(1)).isEqualTo(notification1);
        reset(repository);
    }

    @Test
    void whenDeleteNotificationByRider_thenDelete() throws Exception {
        when(repository.existsById(1L)).thenReturn(true);
        assertThat(service.delete(1)).isNotNull();
        Mockito.verify(repository, VerificationModeFactory.times(1)).existsById(1L);
        reset(repository);
    }
    
}