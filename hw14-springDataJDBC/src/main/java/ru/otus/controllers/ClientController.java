package ru.otus.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.*;

@Controller
public class ClientController {
    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;

    public ClientController(DBServiceClient dbServiceClient, TemplateProcessor templateProcessor)
    {
        this.dbServiceClient = dbServiceClient;
        this.templateProcessor = templateProcessor;
    }

    @GetMapping("/clients")
    @ResponseBody
    public String getAllClients() throws IOException {
        List<Client> clients = dbServiceClient.findAll();
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("clients", clients);
        return templateProcessor.getPage("clients.html", paramsMap);
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
        Phone phone = new Phone();
        phone.setNumber(req.getParameter("phone"));
        phones.add(phone);
        client.setPhones(phones);
        return client;
    }
}
