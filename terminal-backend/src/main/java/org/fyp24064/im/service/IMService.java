package org.fyp24064.im.service;

import lombok.Getter;
import lombok.Setter;
import org.fyp24064.im.model.ChatMessage;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.List;

@Getter
@Setter
public class IMService {
    private final RestTemplate restTemplate;
    public IMService() {
        // Not Autowired as there are no beans created in IM package
        this.restTemplate = new RestTemplate();
    }

    public List<ChatMessage> getChatMessages(String content, int roomId, String sender) {
        /*
        Reason for ParameterizedTypeReference is used, is because at runtime Java does not know
        the type of the generic class (List is declared as List<T>, which T is a generic type, so
        List is a generic class). It erases the type at runtime for generic classes.

        Therefore, if we do not use ParameterizedTypeReference which stores the actual type in runtime,
        then the deserialization of JSON responses will fail.

        If we are only retrieving a single response, we can just do ChatMessage.class
         */
        ResponseEntity<List<ChatMessage>> messageResponseEntity = restTemplate.exchange(
                "",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ChatMessage>>() {}
        );
        return messageResponseEntity.getBody();
    }
}
