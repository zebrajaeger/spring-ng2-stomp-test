package de.zebrajaeger.stomptest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Controller
public class MessageHandlerController {

    private final SimpMessagingTemplate template;

    @Autowired
    public MessageHandlerController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/rpc")
    public void rpcCall(Message message, @Header("correlation-id") String id, @Header("reply-to") String destination) {
        System.out.println("SEND");
        System.out.println(id);
        System.out.println(destination);

        String time = new SimpleDateFormat("HH:mm").format(new Date());
        OutputMessage outputMessage = new OutputMessage(message.getFrom(), message.getText(), time);

        HashMap<String, Object> header = new HashMap<>();
        header.put("correlation-id", id);
        template.convertAndSend(destination, outputMessage, header);
    }

    @MessageMapping("/from-client")
    public void fromClient(Message message) {
        System.out.println("FROM CLIENT");
        System.out.println("  " + message);
    }

    @Scheduled(fixedRate = 1000)
    public void toClient() {
        System.out.println("TO-CLIENT(1)");
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        OutputMessage message = new OutputMessage("to-client(1)", "ping", time);
        template.convertAndSend("/topic/to-client", message);
    }

    /**
     * Does not work for some reason :/
     */
    @Scheduled(fixedRate = 1000)
    @SendTo("/to-client")
    public OutputMessage toClient2() {
        System.out.println("TO-CLIENT(2)");
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage("to-client(2)", "ping", time);
    }
}
