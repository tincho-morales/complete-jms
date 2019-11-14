package activemq.eugen.test.activemq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.*;
import java.util.Map;

public class SampleListener implements MessageListener {

    private JmsTemplate jmsTemplate;
    private Queue queue;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {

                    String msg = ((TextMessage) message).getText();
                    System.out.println("Received message: " + msg);
                    Thread.sleep(5000);
                    if (msg == null) {
                        throw new IllegalArgumentException("Null value received...");
                    }

            }
            if(message instanceof ObjectMessage){
                ObjectMessage objectMessage = (ObjectMessage)message;
                if(objectMessage.getObject() instanceof Employee){
                    Employee employee = (Employee)objectMessage.getObject();
                    System.out.println(employee.toString());
                }
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Employee receiveMessage() throws JMSException {
        Map map = (Map) this.jmsTemplate.receiveAndConvert();
        return new Employee((String) map.get("name"), (Integer) map.get("age"));
    }



}
