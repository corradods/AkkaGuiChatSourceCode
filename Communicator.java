package remote;
//....//
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import java.io.Serializable;
import akka.actor.ActorIdentity;
import akka.actor.Identify;
import akka.actor.Terminated;
import akka.actor.ReceiveTimeout;
import akka.japi.Procedure;
import static java.util.concurrent.TimeUnit.SECONDS;
import scala.concurrent.duration.Duration;
import com.typesafe.config.ConfigFactory;


    public class Communicator extends UntypedActor {

        private GuiChat chat;
        private final String path;
        private ActorRef remoteActor = null;

        public Communicator(String path, GuiChat chat) {
            this.chat = chat;
            this.path = path;
            sendIdentifyRequest();
            //chat.getTextAreaMessages().setText("ok\n");
        }

        private void sendIdentifyRequest() {

            getContext().actorSelection(path).tell(new Identify(path), getSelf());
            getContext().system().scheduler().scheduleOnce(Duration.create(3, SECONDS), getSelf(),ReceiveTimeout.getInstance(), getContext().dispatcher(), getSelf());
        }
        
         private void writeToRoom(String logMessage) {
            
            this.chat.getRoom().setText(this.chat.getRoom().getText()+logMessage);
        }
        @Override
        public void onReceive(Object message) throws Exception {
            if (message instanceof ActorIdentity) {
                remoteActor = ((ActorIdentity) message).getRef();
                if (remoteActor == null) {
                    System.out.println("Remote actor not available: " + path);
                } else {
                    writeToRoom("SUCCESSFULLY CONNECTED TO THE SERVER!!\n");
                    getContext().watch(remoteActor);
                    getContext().become(active, true);
                 }
            } else if (message instanceof ReceiveTimeout) {
                sendIdentifyRequest();
            } else {
                writeToRoom("Not ready yet");
            }
        }

        Procedure<Object> active = new Procedure<Object>() {
        @Override
            public void apply(Object message) {
              
                switch(message.getClass().getSimpleName()) {

                    case "LoginMessage": 
                            remoteActor.tell(message,getSelf());
                             break;

                    case "RejectLogin": 

                            writeToRoom(((Messages.RejectLogin)message).getCause());
                            chat.setBtnLogin(true);
                            chat.setUsername(true);
                            break;

                    case "AckLogin": 

                            writeToRoom("Login Riuscito \n");
                            chat.setClientInput(true);
                            chat.setBtnSend(true);
                            //!! aggiornare la userlist con quella che ricevo nel campo di acklogin
                            break;

                    case "Terminated": 
                            writeToRoom("Calculator terminated");
                            sendIdentifyRequest();
                            getContext().unbecome();
                            break;

                    case "ReceiveTimeout": 
                            //ignore
                                writeToRoom("Ricevuto Receive timeout");
                             break;

                    case "Reply": 
                                    break;
                    
                    case "ChatMessage":
                                    break;

                    case "ToPrintMessage": String msgToPrint = ((Messages.ToPrintMessage)message).getContent();
                                            writeToRoom(msgToPrint);
                    break;
                            

                    default:    unhandled(message);
                                //writeToRoom("Messaggio Sconosciuto: ");
                                //writeToRoom(message.getClass().getSimpleName());
                                //writeToRoom("\n");

                           
                        
      }
  }





               
                
            };
        

    	public static void main(String[] args) {

            GuiChat frame = new GuiChat();
		    frame.setVisible(true);
		    frame.setLocationRelativeTo(null);
		    frame.setTitle("Remote Chat with Akka");
		    

            final ActorSystem system = ActorSystem.create("ClientChatSystem", ConfigFactory.load("remotelookup"));

            final String path = "akka.tcp://ChatSystem@127.0.0.1:2552/user/remoteActor";

            final ActorRef communicator = system.actorOf(Props.create(Communicator.class, path, frame), "communicator");
            
            frame.setActorReference(communicator);
    }

}



