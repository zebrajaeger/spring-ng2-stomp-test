package de.zebrajaeger.stomptest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.HashMap;

@Controller
public class MessageHandlerController {

    private final SimpMessagingTemplate template;
    private int counter = 0;

    @Autowired
    public MessageHandlerController(SimpMessagingTemplate template) {
        this.template = template;
    }

    // every message, the client sends to "/app/from-clients" ends here
    @MessageMapping("/from-client")
    public void foo1(@Payload String message) {
        System.out.println("FROM-CLIENT: " + message);
    }

    // every 10 seconds, we send a message to the client
    @Scheduled(fixedRate = 10000, initialDelay = 0)
    public void toClientTemplate() {
        template.convertAndSend("/topic/to-client", "to-client-" + (counter++));
    }

    // an rpc call ('/app/rpc') from the client doesn't ends here, because we send a response to the required destination.
    // destination is defined in 'my-rx-stomp-rpc.config.ts' as 'replyQueueName'
    @MessageMapping("/rpc")
    public void rpc(@Header("correlation-id") String id,
                    @Header("reply-to") String destination,
                    @Payload String message) {
        System.out.println("RPC('" + id + "', '" + destination + "'): " + message);
        HashMap<String, Object> header = new HashMap<>();
        header.put("correlation-id", id);
        template.convertAndSend(destination, message + "-REPLY", header);
    }

    // errors going directly to users
    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
