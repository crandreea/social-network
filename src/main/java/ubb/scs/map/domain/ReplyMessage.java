package ubb.scs.map.domain;

public class ReplyMessage extends Message {

    private Message repliedMessage;

    public ReplyMessage(Long senderId, Long receiverId, String message, Message repliedMessage) {
        super(senderId, receiverId, message);
        this.repliedMessage = repliedMessage;
    }

    public Message getRepliedMessage() {
        return repliedMessage;
    }

    public void setRepliedMessage(Message repliedMessage) {
        this.repliedMessage = repliedMessage;
    }
}
