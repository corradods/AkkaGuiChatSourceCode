package remote;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

public class RemoteChatServiceActor extends UntypedActor {
  
  //String greeting = "";
  private GuiServer chat;

        
  public RemoteChatServiceActor(GuiServer chat) {
    this.chat = chat;
    //chat.getTextAreaMessages().setText("ok\n");
  }
        
  @Override
  public void onReceive(Object message) {
      String content = this.chat.getTextAreaMessages().getText();
      this.chat.getTextAreaMessages().setText(content+"ok\n");
      // chat.getTextAreaMessages().setText("ok\n");
      /*
      else {
        unhandled(message);
      }
      */
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
