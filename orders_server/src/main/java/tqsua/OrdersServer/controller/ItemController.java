package tqsua.OrdersServer.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tqsua.OrdersServer.model.Item;
import tqsua.OrdersServer.service.ItemService;

@RestController
@RequestMapping("/api")
public class ItemController {

    @Autowired
    private ItemService itemService;

    //@CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(path="/items")
    public ArrayList<Item> getAllItems() throws IOException, InterruptedException {
        return itemService.getAllItems();
    }

}