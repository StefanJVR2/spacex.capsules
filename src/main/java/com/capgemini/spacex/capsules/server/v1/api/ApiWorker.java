package com.capgemini.spacex.capsules.server.v1.api;

import com.capgemini.spacex.capsules.client.models.CapsuleResponse;
import com.capgemini.spacex.capsules.repositories.CapsuleRepository;
import com.capgemini.spacex.capsules.server.v1.mappers.EntityModelMapper;
import com.capgemini.spacex.capsules.server.v1.mappers.ResponseModelMapper;
import com.capgemini.spacex.capsules.server.v1.models.Capsule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class ApiWorker implements CapsulesApi {
    private final Logger logger = LoggerFactory.getLogger(ApiWorker.class);

    private final RestTemplate restTemplate;
    private final ResponseModelMapper responseModelMapper;
    private final EntityModelMapper entityModelMapper;
    private final CapsuleRepository capsuleRepository;
    private final String spacexUrl;

    public ApiWorker(RestTemplate restTemplate, ResponseModelMapper responseModelMapper, EntityModelMapper entityModelMapper,
                     CapsuleRepository capsuleRepository, @Value("${spacex.capsules.url}") String spacexUrl) {

        this.restTemplate = restTemplate;
        this.responseModelMapper = responseModelMapper;
        this.entityModelMapper = entityModelMapper;
        this.capsuleRepository = capsuleRepository;
        this.spacexUrl = spacexUrl;
    }

    /**
     * GET /capsules : Get all capsules information in the services memory store.
     * This call does not fetch information from SpaceX, it only retrieves what has been looked up/posted using /capsules/{capsule_serial}
     *
     * @return OK (status code 200)
     * or No capsules found in memory store (status code 204)
     * or Could not retrieve capsule data from memory store (status code 412)
     */
    @Override
    public ResponseEntity<List<Capsule>> getAllCapsules() {
        logger.info("com.capgemini.spacex.capsules.getAllCapsules");

        var capsuleList = capsuleRepository.findAll();

        if (capsuleList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(entityModelMapper.convert(capsuleList), HttpStatus.OK);
    }

    /**
     * POST /capsules/{capsule_serial} : Save capsule information
     *
     * @param capsuleSerial (required)
     * @return Created (status code 201)
     * or Could not add capsule data to memory store (status code 412)
     */
    @Override
    public ResponseEntity<Void> createCapsuleData(String capsuleSerial) {
        logger.info("com.capgemini.spacex.capsules.createCapsuleData");

        try {
            CapsuleResponse response = restTemplate.getForObject(spacexUrl, CapsuleResponse.class, capsuleSerial);

            var capsule = responseModelMapper.convert(response);
            capsule.getMissions().forEach(mission -> mission.setCapsule(capsule));

            capsuleRepository.save(capsule);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (RestClientException rce) {
            logger.error(rce.getMessage());

            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }

    /**
     * DELETE /capsules/{capsule_serial} : Delete capsule information
     *
     * @param capsuleSerial (required)
     * @return OK (status code 204)
     * or Could not delete capsule from in memory store (status code 412)
     */
    @Override
    public ResponseEntity<Void> deleteCapsuleData(String capsuleSerial) {
        logger.info("com.capgemini.spacex.capsules.deleteCapsuleData");

        if (capsuleRepository.findById(capsuleSerial).isPresent()) {
            capsuleRepository.deleteById(capsuleSerial);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * GET /capsules/{capsule_serial} : Get details about a specific SpaceX capsule, including launches landings and crashes
     *
     * @param capsuleSerial (required)
     * @return OK (status code 200)
     * or Nothing found (status code 204)
     * or Could not fetch capsule details (status code 412)
     */
    @Override
    public ResponseEntity<Capsule> getCapsuleData(String capsuleSerial) {
        logger.info("com.capgemini.spacex.capsules.getCapsuleData");

        if (capsuleRepository.findById(capsuleSerial).isEmpty()) {
            createCapsuleData(capsuleSerial);
        }

        var capsule = capsuleRepository.findById(capsuleSerial);
        return capsule.map(value -> new ResponseEntity<>(entityModelMapper.convert(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED));
    }
}
