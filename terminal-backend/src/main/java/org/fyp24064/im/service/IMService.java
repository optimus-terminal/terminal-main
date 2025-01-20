package org.fyp24064.im.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fyp24064.im.ChatMessage;
import org.fyp24064.im.ChatRoom;
import org.fyp24064.im.model.CreateChatRoom;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.fyp24064.constants.IMConstants.*;

@Service
public final class IMService {
    private static RestTemplate restTemplate = new RestTemplate();

    public static List<ChatRoom> getChatRoomsOfUser(String userId) {
        String URL = String.format(URL_BASE_GET_ROOMS, userId);
        ResponseEntity<List<ChatRoom>> messageResponseEntity = restTemplate.exchange(
                URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ChatRoom>>() {}
        );
        return messageResponseEntity.getBody();
    }

    public static List<ChatMessage> getChatMessages(int roomId) {
        /*
        Reason for ParameterizedTypeReference is used, is because at runtime Java does not know
        the type of the generic class (List is declared as List<T>, which T is a generic type, so
        List is a generic class). It erases the type at runtime for generic classes.

        Therefore, if we do not use ParameterizedTypeReference which stores the actual type in runtime,
        then the deserialization of JSON responses will fail.

        If we are only retrieving a single response, we can just do ChatMessage.class
         */
        String URL = String.format(URL_BASE_GET_MESSAGE, roomId);
        ResponseEntity<List<ChatMessage>> messageResponseEntity = restTemplate.exchange(
                URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ChatMessage>>() {}
        );
        return messageResponseEntity.getBody();
    }

    public static void sendMessageToRoom(String content, int roomId, String sender) throws Exception{
        ChatMessage chatMessage = new ChatMessage(content, roomId, sender);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();
        HttpEntity<?> entity = new HttpEntity<String>(mapper.writeValueAsString(chatMessage), headers);
        ResponseEntity<String> messageResponseEntity = restTemplate.exchange(
                URL_BASE_POST_MESSAGE,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<String>() {}
        );
    }

    public static void sendCreateRoomRequest(String roomTitle, List<String> members) throws Exception{
        CreateChatRoom chatRoom = new CreateChatRoom(roomTitle, members);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper mapper = new ObjectMapper();
        HttpEntity<?> entity = new HttpEntity<String>(mapper.writeValueAsString(chatRoom), headers);
        ResponseEntity<String> messageResponseEntity = restTemplate.exchange(
                URL_BASE_POST_CREATE_ROOM,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<String>() {}
        );
    }
}
