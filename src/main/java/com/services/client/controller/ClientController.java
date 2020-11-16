package com.services.client.controller;

import com.services.client.dto.ClientDTO;
import com.services.client.entity.Client;
import com.services.client.service.ClientService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("clients")
@AllArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<List<Client>> showClients() {
        return ResponseEntity.ok(clientService.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Client> showClientById(@PathVariable UUID id){
        try {
            return ResponseEntity.ok(clientService.getById(id));
        }
        catch (NotFoundException e)
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @PostMapping
    public ResponseEntity<Client> addClient(@RequestBody ClientDTO clientDTO) {
        Client newClient = new Client(clientDTO.getName(), clientDTO.getSportsCategory(),
                clientDTO.getHorsemanStatus());

        return ResponseEntity.ok(clientService.saveClient(newClient));
    }

    @PatchMapping("{id}/spend/{moneyAmount}")
    public ResponseEntity<Client> showClientById(@PathVariable UUID id, @PathVariable int moneyAmount){
        try {
            return ResponseEntity.ok(clientService.spendMoney(id,moneyAmount));
        }
        catch (NotFoundException e)
        {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id){
        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
