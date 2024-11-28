package in.springboot.Main.helper;

import org.springframework.stereotype.Component;

@Component
public class Message {

	private String contect;
	private String type;
	
	public Message() {
		
	}
	
	public Message(String contect, String type) {
		super();
		this.contect = contect;
		this.type = type;
	}
	public String getContect() {
		return contect;
	}
	public void setContect(String contect) {
		this.contect = contect;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
