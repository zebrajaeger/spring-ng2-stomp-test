package de.zebrajaeger.stomptest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class MessageHandlerController {

    private final SimpMessagingTemplate template;
    private AtomicLong counter = new AtomicLong(0);

    @Autowired
    public MessageHandlerController(SimpMessagingTemplate template) {
        this.template = template;
    }

//    @MessageMapping("/xxx")
//    public void send(Message message) {
//        template.convertAndSend("/topic/messages", buildNextMessage());
//    }

    @MessageMapping("/xxx")
    @SendTo("/topic/messages")
    public OutputMessage send(Message message) {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }

    private String buildNextMessage() {
        var message = "Test" + counter.getAndIncrement();
        System.out.println("Send message " + message);
        return message;
    }
}
