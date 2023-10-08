package com.chucktown.neighbors.api.services;

import com.chucktown.neighbors.api.models.RequestType;
import com.chucktown.neighbors.api.repository.RequestTypeRepository;
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
public class RequestTypeService {

    @Autowired
    RequestTypeRepository requestTypeRepository;

    public List<RequestType> getAllRequestTypes() {
        log.info("Entered getAllRequestTypes");

        Iterator<RequestType> iterator = requestTypeRepository.findAll().iterator();
        List<RequestType> newList = new ArrayList<>();
        while(iterator.hasNext()){
            newList.add(iterator.next());
        }
        return newList;
    }

    public RequestType createRequestType(RequestType requestType) {
        log.info("Entered createRequestType");
        log.info("Parameters: [{}]", requestType);

        if (requestType.getId()== null)
            return requestTypeRepository.save(requestType);

        return null;
    }

    public RequestType getRequestType(Long id) {
        log.info("Entered getRequestType");
        log.info("Parameters: {}", id);

        Optional<RequestType> requestType = requestTypeRepository.findById(id);
        if (requestType.isPresent()) {
            return requestType.get();
        }

        return null;
    }

    public RequestType updateRequestType(Long id, RequestType requestType) {
        log.info("Entered updateRequestType");
        log.info("Parameters: [{}] [{}]", id, requestType);

        Optional<RequestType> existing = requestTypeRepository.findById(id);
        if (existing.isPresent()) {
            log.info("RequestType exists.");
            RequestType current = existing.get();
            try {
                Long currentId = current.getId();
                BeanUtils.copyProperties(requestType, current);
                current.setId(currentId);
                return requestTypeRepository.save(current);
            } catch (Exception e) {
                log.error("Error: {}", e.getMessage());
            }
        }

        return null;
    }

    public void deleteRequestType(Long id) {
        log.info("Entered deleteRequestType");
        Optional<RequestType> existing = requestTypeRepository.findById(id);
        if (existing.isPresent()) {
            requestTypeRepository.delete(existing.get());
        }
    }
}
