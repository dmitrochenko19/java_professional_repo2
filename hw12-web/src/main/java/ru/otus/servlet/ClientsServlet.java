package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

@SuppressWarnings({"squid:S1948"})
public class ClientsServlet extends HttpServlet {
    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        List<Client> clients = dbServiceClient.findAll();

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("clients", clients);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage("clients.html", paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        dbServiceClient.saveClient(createClientFromRequest(req));
        doGet(req, resp);
    }

    private Client createClientFromRequest(HttpServletRequest req) {
        Client client = new Client();
        client.setName(req.getParameter("name"));
        Address address = new Address();
        address.setStreet(req.getParameter("address"));
        client.setAddress(address);
        List<Phone> phones = new ArrayList<>();
        Phone phone = new Phone();
        phone.setNumber(req.getParameter("phone"));
        phones.add(phone);
        client.setPhones(phones);
        return client;
    }
}
