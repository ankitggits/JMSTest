package com.example;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class TestProcessor {

	@Autowired
	JmsTemplate template;

	@JmsListener(destination = "my-queue")
	public void receive(TextMessage message) {
		try {
			MessageCreator messageCreator = new MessageCreator() {
				@Override
				public TextMessage createMessage(Session session) throws JMSException {
					String msg = message.getText().concat("-Acknowledgement");
					TextMessage newMessage = session.createTextMessage(msg);
					newMessage.setJMSCorrelationID(message.getJMSCorrelationID());
					return newMessage;
				}
			};
			System.out.println("Processed         --->" + message.getText());
			template.send("my-queue-done", messageCreator);
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}
}
