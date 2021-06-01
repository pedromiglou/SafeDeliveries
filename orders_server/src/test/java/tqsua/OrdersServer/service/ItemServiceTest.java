package tqsua.OrdersServer.service;


import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tqsua.OrdersServer.model.Item;
import tqsua.OrdersServer.repository.ItemRepository;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    
    @Mock
    private ItemRepository repository;

    @InjectMocks
    private ItemService service;

    @Test
    void whenGetAllItem_thenReturnCorrectResults() throws Exception {
        ArrayList<Item> response = new ArrayList<>();
        Item item1 = new Item("TV", "Informatica", 43.0);
        Item item2 = new Item("Frigorifico", "Eletrodomesticos", 290.6);
        response.add(item1);
        response.add(item2);

        when(repository.findAll()).thenReturn(response);
        assertThat(service.getAllItems()).isEqualTo(response);
        reset(repository);
    }

}