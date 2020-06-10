package io.github.majusko.pulsar;

import org.springframework.stereotype.Component;

@Component
public class TestService {

	public void print(Object o) {
		System.out.println("print==="+o);
	}
}
