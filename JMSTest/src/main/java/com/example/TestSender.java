package com.example;

import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import rx.Observable;

public class TestSender{

	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	TestReceiver receiver;
	
	public FlowStatus send(String msg){
		FlowStatus flowStatus = new FlowStatus();
		flowStatus.status=false;
		String uniqueId = "APP-1-"+UUID.randomUUID().toString();
		
	    MessageCreator messageCreator = new MessageCreator() {
	        @Override
	        public TextMessage createMessage(Session session) throws JMSException {
	        	TextMessage message = session.createTextMessage(msg);
	        	message.setJMSCorrelationID(uniqueId);
	        	message.setJMSExpiration(Constants.timeout);
	            return message;
	        }
	    };
	    System.out.println("Sent              --->"  + msg);
	    jmsTemplate.send("my-queue", messageCreator);
	    
	    Observable<TextMessage> observable = receiver.returnResponse(uniqueId);

	    observable.subscribe((message) -> {
			 try {
				 System.out.println("Received          --->" + message.getText());
				 flowStatus.status = true;
				 flowStatus.callbackMessage = message.getText();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	    return flowStatus;
	}
	
	
}
