package com.services.client.rabbitmq;

import com.services.client.controller.rest.dto.ClientDTO;
import com.services.client.entity.Client;
import com.services.client.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Server {
    private final ClientService clientService;

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void addClient(ClientDTO clientDTO){
        Client newClient = new Client(clientDTO.getName(),clientDTO.getSportsCategory(),clientDTO.getHorsemanStatus());
        clientService.saveClient(newClient);
    }
}
