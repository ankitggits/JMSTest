package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class JmsTestApplication {


//	@Bean
//	ConnectionFactory connectionFactory(){
//		return new CachingConnectionFactory(new ActiveMQConnectionFactory("tcp://localhost:61616"));
//	}
//	
//	@Bean
//	JmsTemplate jmsTemplate(ConnectionFactory factory){
//		return new JmsTemplate(factory);
//	}
	
	public static void main(String[] args) { 
        SpringApplication.run(JmsTestApplication.class, args);
	}
	
	@Bean
	public TestSender sender(){
		return new TestSender();
	}
	
	@Bean
	public TestReceiver receiver(){
		return new TestReceiver();
	}
	
	@Bean
	public TestProcessor processor(){
		return new TestProcessor();
	}
}
