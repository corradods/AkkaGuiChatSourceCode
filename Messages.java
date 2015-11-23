package remote;

import java.io.Serializable;

import akka.actor.ActorRef;

public class Messages implements Serializable{
	
	class ChatMessage implements Serializable {
    	private String content;
    
    	public ChatMessage(String s){ content = s;}
        public String GetContent() {return content;}	
    	

	}

	class LoginMessage implements Serializable {
		
		private String nickname;

		public LoginMessage(String nickname){ this.nickname = nickname;}
		public String GetNickname() {return nickname;}	
		
	}
	
	class LogoutMessage implements Serializable {
		
		private String nickname;

		public LoginMessage(String nickname){ this.nickname = nickname;}
		public String GetNickname() {return nickname;}	
		
	}
	class RejectLogin implements Serializable {
		
		private String cause;

		public RejectLogin(String cause){
			this.cause = cause;
		}
	}
	class AckLogin implements Serializable {
		
		private String s;

		public LoginMessage(String s){
			this.s = s;
		}
	}
}
