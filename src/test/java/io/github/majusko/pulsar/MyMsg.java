package io.github.majusko.pulsar;

public class MyMsg {
	
	public MyMsg() {
	}
	
    private String data;
    public MyMsg(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
