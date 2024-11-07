package org.fyp24064.im.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;


@Service
@Getter
@Setter
@AllArgsConstructor
public class ChatService {
    // TODO: Set up chat rooms
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;
}
