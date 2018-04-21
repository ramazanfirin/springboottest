package com.mastertech.springboottest.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mastertech.springboottest.domain.UserRecord;
import com.mastertech.springboottest.repository.UserRepository;
import com.mastertech.springboottest.service.UserService;

import io.micrometer.core.annotation.Timed;


@RestController
@RequestMapping("/api")
public class UserController {
	
	 private final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;

	@Autowired  
    private UserRepository userRepository;
	
	
	
	@PostMapping("/users")
    @Timed
    public ResponseEntity<UserRecord> createCity(@Valid @RequestBody UserRecord userRecord) throws URISyntaxException {
        log.debug("REST request to save UserRecord : {}", userRecord);
        if (userRecord.getId() != null) {
            return ResponseEntity.badRequest().header("ErrorHeader","There is id in request").body(null);
        }
        UserRecord result = userRepository.save(userRecord);
        return ResponseEntity.created(new URI("/api/cities/" + result.getId()))
            .header("message","created")
            .body(result);
    }
	
	@GetMapping("/users")
    @Timed
    public List<UserRecord> getAllFaqTypes() {
        log.debug("REST request to get all FaqTypes");
        return userRepository.findAll();
    }
	
	
	@GetMapping("/users/{id}")
    @Timed
    public ResponseEntity<UserRecord> getFaqType(@PathVariable Long id) {
        log.debug("REST request to get FaqType : {}", id);
        Optional<UserRecord> userRecord = userRepository.findById(id);
        return userRecord.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        
        //return ResponseUtil.wrapOrNotFound(Optional.ofNullable(faqType));
    }
	
	
	@PutMapping("/users")
    @Timed
    public ResponseEntity<UserRecord> updateFaqType(@RequestBody UserRecord userRecord) throws URISyntaxException {
        log.debug("REST request to update FaqType : {}", userRecord);
        if (userRecord.getId() == null) {
            return createCity(userRecord);
        }
        UserRecord result = userRepository.save(userRecord);
      
        return ResponseEntity.ok()
            //.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, faqType.getId().toString()))
            .body(result);
    }
	
	@DeleteMapping("/users/{id}")
    @Timed
    public ResponseEntity<Void> deleteFaqType(@PathVariable Long id) {
        log.debug("REST request to delete FaqType : {}", id);
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

	
}
