package tqsua.OrdersServer.service;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqsua.OrdersServer.model.Item;
import tqsua.OrdersServer.repository.ItemRepository;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public ArrayList<Item> getAllItems() throws IOException, InterruptedException {
        return this.itemRepository.findAll();
    }

}