package activemq.eugen.test.activemq;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.jmx.ManagementContext;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.io.File;


@Configuration
public class ActiveMQConfiguration {

    public final String DESTINATION_QUEUE_NAME = "tincho";

    @Bean
    public JmsTemplate jmsTemplate(){

        JmsTemplate jmsTemplate = new JmsTemplate ();
        jmsTemplate.setConnectionFactory(pooledConnectionFactory());
        jmsTemplate.setDefaultDestination(activeMQQueue());
        jmsTemplate.setMessageConverter(sampleMessageConverter());
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());

        return jmsTemplate;
    }

    @Bean
    public ActiveMQQueue activeMQQueue(){

        return new ActiveMQQueue(DESTINATION_QUEUE_NAME);
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory(){

        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL("tcp://localhost:61616");

        return activeMQConnectionFactory;
    }


    @Bean
    public PooledConnectionFactory pooledConnectionFactory(){
        return new PooledConnectionFactory(activeMQConnectionFactory());
    }

    @Bean
    public SampleMessageConverter sampleMessageConverter(){
        return new SampleMessageConverter();
    }

    @Bean
    public SampleListener sampleListener(){

        SampleListener sampleListener = new SampleListener();

        sampleListener.setJmsTemplate(jmsTemplate());
        sampleListener.setQueue(activeMQQueue());

        return sampleListener;
    }

    @Bean
    public SampleJmsErrorHandler sampleJmsErrorHandler(){
        return new SampleJmsErrorHandler();
    }

    @Bean
    public DefaultMessageListenerContainer defaultMessageListenerContainer(){

        DefaultMessageListenerContainer defaultMessageListenerContainer = new DefaultMessageListenerContainer();

        defaultMessageListenerContainer.setConnectionFactory(pooledConnectionFactory());
        defaultMessageListenerContainer.setDestination(activeMQQueue());
        defaultMessageListenerContainer.setMessageListener(sampleListener());
        defaultMessageListenerContainer.setErrorHandler(sampleJmsErrorHandler());
        defaultMessageListenerContainer.setConcurrentConsumers(5);
        defaultMessageListenerContainer.setMaxConcurrentConsumers(10);

        return defaultMessageListenerContainer;
    }

    @Bean
    public SampleJmsMessageSender sampleJmsMessageSender(){

        SampleJmsMessageSender sampleJmsMessageSender = new SampleJmsMessageSender();

        sampleJmsMessageSender.setJmsTemplate(jmsTemplate());
        sampleJmsMessageSender.setQueue(activeMQQueue());

        return sampleJmsMessageSender;
    }


    @Bean
    public BrokerService broker() throws Exception {

        final BrokerService broker = new BrokerService();

        broker.addConnector("tcp://localhost:61616");

        KahaDBPersistenceAdapter kahaDBPersistenceAdapter = new KahaDBPersistenceAdapter();
        File file  = new File("data");
        kahaDBPersistenceAdapter.setDirectory(file);
        broker.setPersistenceAdapter(kahaDBPersistenceAdapter);

        final ManagementContext managementContext = new ManagementContext();
        broker.setManagementContext(managementContext);

        return broker;
    }


    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}
