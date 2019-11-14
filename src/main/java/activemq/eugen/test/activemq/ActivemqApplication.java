package activemq.eugen.test.activemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ActivemqApplication implements ApplicationRunner {

	@Autowired
	private SampleJmsMessageSender sampleJmsMessageSender;

	public static void main(String[] args) {

		SpringApplication.run(ActivemqApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments applicationArguments) throws Exception {
		/*
		for(int i=0;i<100;i++){
			sampleJmsMessageSender.sendTextMessage(Integer.toString(i));
		}*/
		sampleJmsMessageSender.sendMessage(new Employee("Tincho",28));
	}

}
