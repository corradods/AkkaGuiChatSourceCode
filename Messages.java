package remote;

import java.io.Serializable;

import akka.actor.ActorRef;

public class Messages implements Serializable{
	
	class Altro implements Serializable {
    	private String content;
    
    	public Altro(String s){
        	content = s;
    	}

	}
	class LoginMessage implements Serializable {
		private ActorRef actor;
		private String nickname;

		
		public LoginMessage(ActorRef actor){
			this.actor = actor;
			this.nickname = "prova";
		}
	}
	class DisconnectMessage implements Serializable {
		
	}
}
