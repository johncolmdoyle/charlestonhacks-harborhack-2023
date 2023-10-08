package com.chucktown.neighbors.api.services;

import com.chucktown.neighbors.api.models.Address;
import com.chucktown.neighbors.api.models.Client;
import com.chucktown.neighbors.api.repository.ClientRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    private final String googleMapsApiKey = "AIzaSyAgPUh-IbknOFMv56Yms23HK1cR0LW_KgY";

    public List<Client> getAllClients() {
        log.info("Entered getAllClients");

        // get all users from database
        Iterator<Client> iterator = clientRepository.findAll().iterator();
        List<Client> newList = new ArrayList<>();
        while(iterator.hasNext()){
            newList.add(iterator.next());
        }
        return newList;
    }

    public Client createClient(Client client) {
        log.info("Entered createClient");
        log.info("Parameters: [{}]", client);

        Optional<Client> existingClient = clientRepository.findByAuth0Id(client.getAuth0Id());
        // save to database
        if (!existingClient.isPresent()) {
            if (client.getAddress() != null) {
                client.setAddress(getGoogleAddress(client.getAddress()));
            }
            return clientRepository.save(client);
        }

        return existingClient.get();
    }

    public Client getClient(Long id) {
        log.info("Entered getClient");
        log.info("Parameters: {}", id);

        // get user from database
        Optional<Client> user = clientRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }

        return null;
    }

    public Client getClientByAuth0Id(String auth0Id) {
        log.info("Entered getClientByAuth0Id");
        log.info("Parameters: {}", auth0Id);

        // get user from database
        Optional<Client> user = clientRepository.findByAuth0Id(auth0Id);
        if (user.isPresent()) {
            return user.get();
        }

        return null;
    }

    public Client updateClient(Long id, Client client) {
        log.info("Entered updateClient");
        log.info("Parameters: [{}] [{}]", id, client);

        // get user from database
        Optional<Client> user = clientRepository.findById(id);
        if (user.isPresent()) {
            log.info("User exists.");
            Client currentClient = user.get();
            if (client.getAddress() != null) {
                client.setAddress(getGoogleAddress(client.getAddress()));
            }
            try {
                Long currentId = currentClient.getId();
                BeanUtils.copyProperties(client, currentClient);
                currentClient.setId(currentId);
                return clientRepository.save(currentClient);
            } catch (Exception e) {
                log.error("Error: {}", e.getMessage());
            }
        }

        return null;
    }

    public void deleteClient(Long id) {
        log.info("Entered deleteClient");
        log.info("Parameters: {}", id);

        Optional<Client> user = clientRepository.findById(id);
        if (user.isPresent()) {
            clientRepository.delete(user.get());
        }
    }

    public Address getGoogleAddress(Address address) {
        log.info("Entered getGoogleAddress");

        try {
            String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + URIUtil.encodeQuery(address.getFormattedAddress()) + "&key=" + googleMapsApiKey;

            // Create a URL object and open a connection to the API endpoint
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the HTTP request method to GET
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response from the API
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                // Print the JSON response
                log.info(response.toString());
                ObjectMapper mapper = new ObjectMapper();
                JsonNode responseNode = mapper.readTree(response.toString());
                address.setLatitude(Double.parseDouble(responseNode.get("results").get(0).get("geometry").get("location").get("lat").asText()));
                address.setLongitude(Double.parseDouble(responseNode.get("results").get(0).get("geometry").get("location").get("lng").asText()));
            } else {
                log.error("HTTP request failed with response code: " + responseCode);
            }
        } catch (IOException e) {
            log.error("Exception: {}", e.getMessage());
            e.printStackTrace();
        }

        return address;
    }
}
