package com.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;

import rx.Observable;

public class TestReceiver{
	
	@Autowired
	JmsTemplate template;
	
	private final Map<String, TextMessage> messageStore = new ConcurrentHashMap<String, TextMessage>();

	@JmsListener(destination = "my-queue-done")
    public void receive(TextMessage message) {
		try {
			messageStore.put(message.getJMSCorrelationID(), message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
    }
    
	public Observable<TextMessage> returnResponse(String correlation){
		Observable<TextMessage> futureMessage = null;
    	while(true){
    		if(messageStore.containsKey(correlation)){
    			futureMessage = Observable.just(messageStore.get(correlation));
    			messageStore.remove(correlation);
        		break;
    		}
    		continue;
    	}
		return futureMessage;
    }
   
}
