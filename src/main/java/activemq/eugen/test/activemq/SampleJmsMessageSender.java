package activemq.eugen.test.activemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Queue;


public class SampleJmsMessageSender {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private Queue queue;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public void simpleSend() {

        jmsTemplate.send(queue, s -> s.createTextMessage("hello queue world"));
    }

    public void sendMessage(final Employee employee) {

        this.jmsTemplate.convertAndSend(employee);
    }

    public void sendTextMessage(String msg) {

        this.jmsTemplate.send(queue, s -> s.createTextMessage(msg));
    }
}