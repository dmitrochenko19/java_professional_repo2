package ru.otus.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;

import java.io.IOException;
import java.util.*;

@Controller
public class ClientController {
    private final DBServiceClient dbServiceClient;

    public ClientController(DBServiceClient dbServiceClient)
    {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping("/clients")
    public String getAllClients(Model model) {
        List<Client> clients = dbServiceClient.findAll();
        if (model.getAttribute("client") == null) {
            model.addAttribute("client", new Client());
        }
        model.addAttribute("clients", clients);
        return "clients";
    }

    @PostMapping("/clients")
    public RedirectView addClient(HttpServletRequest req)  {
        Client client = createClient(req);
        dbServiceClient.saveClient(client);
        return new RedirectView("/clients", true);
    }

    private Client createClient(HttpServletRequest req)
    {
        Client client = new Client();
        client.setName(req.getParameter("name"));
        Address address = new Address();
        address.setStreet(req.getParameter("address"));
        client.setAddress(address);
        Set<Phone> phones = new HashSet<>();
        for (String num : req.getParameter("phones").split(",")) {
            Phone phone = new Phone();
            phone.setNumber(num);
            phones.add(phone);
        }
        client.setPhones(phones);
        return client;
    }
}
