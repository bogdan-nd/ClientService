package com.services.client.controller.grpc;

import com.services.client.entity.Client;
import com.services.client.enums.HorsemanStatus;
import com.services.client.enums.SportsCategory;
import com.services.client.service.ClientService;
import com.services.grpc.server.horse.*;
import io.grpc.stub.StreamObserver;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@GrpcService
@AllArgsConstructor
public class ClientGrpcServer extends ClientServiceGrpc.ClientServiceImplBase {
    private final ClientService clientService;

    @Override
    public void getClients(Empty request, StreamObserver<ClientResponse> responseObserver) {
        List<Client> clients = clientService.findAll();
        List<ProtoClient> protoClients = transformClients(clients);

        ClientResponse response = ClientResponse.newBuilder()
                .addAllClient(protoClients).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getClientById(IdRequest request, StreamObserver<ClientResponse> responseObserver) {
        UUID id = UUID.fromString(request.getId());
        ClientResponse response;
        try {
            Client client = clientService.getById(id);
            ProtoClient protoClient = transformClientToProto(client);
            response = ClientResponse.newBuilder()
                    .addClient(protoClient).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (NotFoundException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void addClient(ClientRequest request, StreamObserver<ClientResponse> responseObserver) {
        ProtoClient protoClient = request.getHorse();
        Client client = createClientFromProto(protoClient);

        Client createdHorse = clientService.saveClient(client);
        ProtoClient createdProtoHorse = transformClientToProto(createdHorse);

        ClientResponse response = ClientResponse.newBuilder()
                .addClient(createdProtoHorse)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void spendMoney(MoneyRequest request, StreamObserver<StringResponse> responseObserver) {
        String id = request.getId();
        UUID clientId = UUID.fromString(id);
        int money = request.getMoneyAmount();
        StringResponse response;

        try {
            clientService.clientSpendMoney(clientId, money);
            response = StringResponse.newBuilder()
                    .setAnswer(String.format("Client with %s id spend %s $", clientId,money))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        catch (NotFoundException e){
            responseObserver.onError(e);
        }
    }

    @Override
    public void earnMoney(MoneyRequest request, StreamObserver<StringResponse> responseObserver) {
        String id = request.getId();
        UUID clientId = UUID.fromString(id);
        int money = request.getMoneyAmount();
        StringResponse response;

        try {
            clientService.clientEarnMoney(clientId, money);
            response = StringResponse.newBuilder()
                    .setAnswer(String.format("Client with %s id earned %s $", clientId,money))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        catch (NotFoundException e){
            responseObserver.onError(e);
        }
    }

    private Client createClientFromProto(ProtoClient protoClient) {
        String name = protoClient.getName();
        HorsemanStatus status = HorsemanStatus.valueOf(protoClient.getHorsemanStatus());
        SportsCategory sportsCategory = SportsCategory.valueOf(protoClient.getSportCategory());

        return new Client(name,sportsCategory,status);
    }

    private ProtoClient transformClientToProto(Client client) {
        String clientId = client.getId().toString();
        String horsemanStatus = client.getHorsemanStatus().toString();
        String sportCategory = client.getSportCategory().toString();

        ProtoClient protoClient = ProtoClient.newBuilder()
                .setId(clientId)
                .setName(client.getName())
                .setCreditMoney(client.getCreditMoney())
                .setHorsemanStatus(horsemanStatus)
                .setSportCategory(sportCategory)
                .build();

        return protoClient;
    }

    private List<ProtoClient> transformClients(List<Client> clients) {
        List<ProtoClient> protoClients = new ArrayList<>();

        for (Client client : clients) {
            ProtoClient protoClient = transformClientToProto(client);
            protoClients.add(protoClient);
        }

        return protoClients;
    }
}
