package io.github.majusko.pulsar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { PulsarJavaSpringBootStarterApplication.class}) // 指定启动类
public class ConsumerTest {


	@Test
	public void testConsumer() throws Exception {
		Thread.sleep(100000);
	}
	

}
