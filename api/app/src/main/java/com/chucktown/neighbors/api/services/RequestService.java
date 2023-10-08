package com.chucktown.neighbors.api.services;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;
import com.chucktown.neighbors.api.models.Assister;
import com.chucktown.neighbors.api.models.AssisterState;
import com.chucktown.neighbors.api.models.Client;
import com.chucktown.neighbors.api.models.Request;
import com.chucktown.neighbors.api.repository.ClientRepository;
import com.chucktown.neighbors.api.repository.RequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RequestService {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    ClientRepository clientRepository;

    public List<Request> getAllRequests() {
        log.info("Entered getAllRequests");

        Iterator<Request> iterator = requestRepository.findAll().iterator();
        List<Request> newList = new ArrayList<>();
        while(iterator.hasNext()){
            newList.add(iterator.next());
        }
        return newList;
    }

    public Request createRequest(Request request) {
        log.info("Entered createRequest");
        log.info("Parameters: [{}]", request);

        List<Client> notitficationList = clientRepository.findClientsWithinDistance(
                request.getRequestor().getAddress().getLatitude(),
                request.getRequestor().getAddress().getLongitude()
                );
        log.info("Found " + notitficationList.size() + " clients in the area");

            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard()
                            .withRegion(Regions.US_EAST_1).build();

            // Loop over the notification list
            for (Client emailClient : notitficationList) {
                try {
                    SendTemplatedEmailRequest emailRequest = new SendTemplatedEmailRequest()
                            .withDestination(
                                    new Destination().withToAddresses(emailClient.getEmail()))
                            .withConfigurationSetName("Default")
                            .withTemplate("RequestTemplate")
                            .withTemplateData("{\"name\":\""+emailClient.getFirstName()+"\"}")
                            .withSource("callout@chucktownneighbors.com");
                    client.sendTemplatedEmail(emailRequest);
                } catch (Exception e) {
                    log.error("Exception: {}", e.getMessage());
                }
            }


        if (request.getId()== null)
            return requestRepository.save(request);

        return null;
    }

    public Request getRequest(Long id) {
        log.info("Entered getRequest");
        log.info("Parameters: [{}]", id);

        Optional<Request> request = requestRepository.findById(id);
        if (request.isPresent()) {
            return request.get();
        }

        return null;
    }

    public Request getRequestByClient(Long id, Client client) {
        log.info("Entered getRequestByClient");
        log.info("Parameters: [{}] [{}]", id, client);

        Optional<Request> request = requestRepository.findByIdAndRequestor(id, client);
        if (request.isPresent()) {
            return request.get();
        }

        return null;
    }

    public Request updateRequest(Long id, Request request) {
        log.info("Entered updateRequest");
        log.info("Parameters: [{}] [{}]", id, request);

        Optional<Request> existing = requestRepository.findById(id);
        if (existing.isPresent()) {
            log.info("Request exists.");
            Request current = existing.get();
            try {
                Long currentId = current.getId();
                BeanUtils.copyProperties(request, current);
                current.setId(currentId);
                return requestRepository.save(current);
            } catch (Exception e) {
                log.error("Error: {}", e.getMessage());
            }
        }

        return null;
    }

    public void deleteRequest(Long id) {
        log.info("Entered deleteRequest");
        Optional<Request> existing = requestRepository.findById(id);
        if (existing.isPresent()) {
            requestRepository.delete(existing.get());
        }
    }

    public Request offerAssistance(Long id, Client client) {
        log.info("Entered offerAssistance");
        log.info("Parameters: [{}] [{}]", id, client);

        Optional<Request> requestCheck = requestRepository.findById(id);
        if (requestCheck.isPresent()) {
            Request request = requestCheck.get();

            Assister assister = new Assister();
            assister.setAssister(client);
            assister.setAssisterState(AssisterState.NEW);
            assister.setRequesterState(AssisterState.NEW);
            log.info("Adding assister: {}", assister);

            request.addAssistor(assister);

            return requestRepository.save(request);
        } else {
            return null;
        }
    }

    public Request updateAssistance(Long id, Long assisterId, Client client, AssisterState assisterState) {
        log.info("Entered updateAssistance");
        log.info("Parameters: [{}] [{}] [{}] [{}]", id, assisterId, client, assisterState);

        Optional<Request> requestCheck = requestRepository.findById(id);
        if (requestCheck.isPresent()) {
            log.info("Request found.");
            Request request = requestCheck.get();

            // requester
            if (request.getRequestor().getId().equals(client.getId())) {
                log.info("Update by requester");
                Assister assister = request.getAssister(assisterId);
                assister.setRequesterState(assisterState);
            }
            // assister
            else {
                log.info("Update by assister");
                Assister assister = request.getAssister(assisterId);
                assister.setAssisterState(assisterState);
            }

            return requestRepository.save(request);
        } else {
            return null;
        }
    }
}
