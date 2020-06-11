package io.github.majusko.pulsar;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.pulsar.client.api.SubscriptionType;

import io.github.majusko.pulsar.config.ConsumerConfigurationDataExt;
import io.github.majusko.pulsar.config.ProducerConfigurationDataExt;

public class Test {
	
	public static void main(String[] args) throws Exception{
		/*ProducerConfigurationDataExt configurationData = new ProducerConfigurationDataExt();
		
		System.out.println(configurationData.toMap());
		
		ConsumerConfigurationDataExt configurationDataExt = new ConsumerConfigurationDataExt<>();
		configurationDataExt.setSubscriptionType(SubscriptionType.Failover);
		Map map = configurationDataExt.toMap();
		System.out.println(map);*/
		Method declaredMethod = Test.class.getDeclaredMethod("test", String.class);
		System.out.println(declaredMethod.toGenericString());
		
		System.out.println(methodSign("",declaredMethod));
		
	}
	
	@org.junit.Test
	public void testA() {
		System.out.println("=============");
	}
	
	public void test(String s) {
		
	}
	
	public static String methodSign(String className,Method method) {
		StringBuilder builder = new StringBuilder();
		if(method == null) {
			return builder.toString();
		}
		builder.append(className).append(method.getName());
		return builder.toString();
	}

}
