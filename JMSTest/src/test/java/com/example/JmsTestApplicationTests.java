package com.example;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JmsTestApplication.class)
public class JmsTestApplicationTests {

	@Autowired
	TestSender sender;

	@Autowired
	TestReceiver receiver;

	@Autowired
	TestProcessor processor;

	@Autowired
	ConnectionFactory connectionFactory;
	
	Connection conn;

	@Before
	public void setup() throws Exception {
		conn = connectionFactory.createConnection();
		conn.start();
	}
	
	@Test
	public void contextLoads() throws Exception {
		int iteration = 10;
		List<FlowStatus> successStatus = new CopyOnWriteArrayList<>();
		for (int i = 1; i <= iteration; i++) {
			String msg = "message-" + i;
			String expectedMsg = msg.concat("-Acknowledgement");
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					FlowStatus  flowStatus = sender.send(msg);
					if (flowStatus!=null && flowStatus.status && flowStatus.callbackMessage.equals(expectedMsg)) {
						successStatus.add(flowStatus);
					}
				}
			}, "thread-" + i); 
			thread.start();
		}
		Thread.sleep(10000);
		Assert.assertTrue(iteration == successStatus.size());
	}
	
	@After
	public void close() throws Exception{
		System.out.println("-----------The End------------");
	}

}
