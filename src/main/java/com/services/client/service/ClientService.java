package com.services.client.service;

import com.services.client.entity.Client;
import com.services.client.repo.ClientRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {
    private final ClientRepository repository;

    @Autowired
    public ClientService(ClientRepository repository){
        this.repository = repository;
    }

    @Transactional
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Client saveClient(Client client) {
        return repository.save(client);
    }

    @Transactional
    public Client spendMoney(UUID id, int moneyAmount) throws NotFoundException {
        Client client = getById(id);
        client.spendMoney(moneyAmount);
        saveClient(client);
        return client;
    }

    @Transactional
    public Client getById(UUID id) throws NotFoundException {
        Optional<Client> client = repository.findById(id);
        if(client.isPresent())
            return client.get();
        else
            throw new NotFoundException(String.format("Client with %s id does not exist",id));
    }

    @Transactional
    public void deleteById(UUID id){
        repository.deleteById(id);
    }
}
