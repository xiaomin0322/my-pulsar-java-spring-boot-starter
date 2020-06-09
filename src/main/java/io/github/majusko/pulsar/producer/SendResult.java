/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.majusko.pulsar.producer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.pulsar.client.api.MessageId;

/**
 * Created zzm
 */
public class SendResult {

	private int code;
	private String msgId;
	private String msg;
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
	public static SendResult FAILURE = new SendResult(404, "slave节点不存在");
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
