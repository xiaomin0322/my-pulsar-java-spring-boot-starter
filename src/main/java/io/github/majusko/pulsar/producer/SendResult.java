
package io.github.majusko.pulsar.producer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.pulsar.client.api.MessageId;

/**
 * 
 * 消息返回对象
 *
 * @author Zengmin.Zhang
 *
 */
public class SendResult {

	/**
	 * 状态码
	 */
	private int code;

	/**
	 * 状态信息
	 */
	private String msg;
	/**
	 * 消息ID
	 */
	private String msgId;

	/**
	 * 异步返回对象
	 */
	private CompletableFuture<MessageId> sendAsync;

	public SendResult(CompletableFuture<MessageId> sendAsync) {
		this.sendAsync = sendAsync;
	}

	public SendResult getAsyncResult() {
		try {
			MessageId messageId = sendAsync.get();
			return getSendResult(messageId);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static SendResult getSendResult(MessageId messageId) {
		SendResult sendResult = null;
		if (messageId == null) {
			sendResult = SendResult.FAILURE;
		} else {
			sendResult = SendResult.SUCCESS;
			sendResult.setMsgId(messageId.toString());
		}
		return sendResult;
	}

	public SendResult(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public SendResult(int code, String msg, String msgId) {
		this.code = code;
		this.msg = msg;
		this.msgId = msgId;
	}

	public int getCode() {
		return code;
	}

	public String getMsgId() {
		return msgId;
	}

	public String getMsg() {
		return msg;
	}

	static SendResult buildSuccessResult(String msgId) {
		return new SendResult(200, msgId);
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static SendResult SUCCESS = new SendResult(200, null);
	public static SendResult FAILURE_NOTRUNNING = new SendResult(401, "client状态不是running");
	public static SendResult FAILURE_TIMEOUT = new SendResult(402, "客户端发送超时");
	public static SendResult FAILURE_INTERUPRION = new SendResult(403, "等待线程被中断");
	public static SendResult FAILURE = new SendResult(404, "发送消息失败");
	public static SendResult FAILURE_NULL = new SendResult(400, "参数为NULL");

	public static SendResult buildErrorResult(String msg) {
		return new SendResult(404, msg);
	}

	public static SendResult buildErrorResult(int code, String msg) {
		return new SendResult(code, msg);
	}

	public boolean isSucceed() {
		return this.code == 200;
	}

}
