package com.backend.redis.messagelistener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

public class ExpireMessageLinstener extends KeyExpirationEventMessageListener {

    /**
     * Creates new {@link MessageListener} for {@code __keyevent@*__:expired} messages.
     *
     * @param listenerContainer must not be {@literal null}.
     */
    public ExpireMessageLinstener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }


    @Override
    protected void doHandleMessage(Message message) {
        super.doHandleMessage(message);
        byte[] body = message.getBody();
        String result = new String(body);
        System.out.println(result);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        super.onMessage(message, pattern);
    }


}
