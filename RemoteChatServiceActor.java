package remote;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import java.util.HashMap;

public class RemoteChatServiceActor extends UntypedActor {
  
  
  private GuiServer chat;
  private HashMap <ActorRef,String> users;
  private Messages messages;
        
  public RemoteChatServiceActor(GuiServer chat) {
    this.chat = chat;
    messages = new Messages();
    users = new HashMap<>();

    /*
    for(ActorRef ref : users.keySet())
        ref.tell(,getSelf());
    */
  }

  private void writeToLog(String logMessage) {
      this.chat.getLog().setText(this.chat.getLog().getText()+logMessage);
  }
        
  @Override
  public void onReceive(Object message) {
      
      switch(message.getClass().getSimpleName()) {

        case "LoginMessage": 
                            handleLoginMessage(message); 
                            break;

        case "LogoutMessage": 
                            //handleLogoutMessage(message); 
                             break;

        case "ChatMessage": 
                            //handleChatMessage(message); 
                            break;

        default: 
                writeToLog("Messaggio non riconosciuto!...");
                break;
                        
      }
  }

  private void handleLoginMessage(Object message) {
          writeToLog("ok");
  }

  public static void main(String[] args) {

    GuiServer frame = new GuiServer();
    frame.setVisible(true);
    frame.setLocationRelativeTo(null);
    frame.setTitle("Server - Akka Chat");

    final ActorSystem system = ActorSystem.create("ChatSystem", ConfigFactory.load(("chat")));

    final ActorRef remoteActor =system.actorOf(Props.create(RemoteChatServiceActor.class,frame), "remoteActor");

    frame.setActorReference(remoteActor);
    frame.printBootstrapMessage();
  }

   
}
