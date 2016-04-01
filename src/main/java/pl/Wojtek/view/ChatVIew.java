package pl.Wojtek.view;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import pl.Wojtek.model.Game;
import pl.Wojtek.model.Message;
import pl.Wojtek.model.User;
import pl.Wojtek.util.Broadcaster;

import java.util.Objects;

public class ChatView extends VerticalLayout implements View {
    private TextField input;
    public Panel chatContentPanel;
    public VerticalLayout chatContent;
    private HorizontalLayout chatControl;
    private User user;

    public ChatView() {
        user = (User) VaadinSession.getCurrent().getSession().getAttribute("user");

        chatContent = new VerticalLayout();
        chatControl = new HorizontalLayout();

        chatContentPanel = new Panel("Chat");
        chatContentPanel.setHeight("600px");
        chatContentPanel.setContent(chatContent);


        chatControl.setSizeFull();

        Button sendButton = new Button("Send");
        sendButton.setClickShortcut(KeyCode.ENTER);
        sendButton.addClickListener(new SendMessageButton());
        chatControl.addComponent(sendButton);

        input = new TextField();
        input.setWidth("100%");
        chatControl.addComponent(input);

        addComponent(chatContentPanel);
        addComponent(chatControl);
    }

    @Override
    public void enter(ViewChangeEvent event) {
        String message = input.getValue();
        sendMessage(message);
    }

    public class SendMessageButton implements Button.ClickListener {
        @Override
        public void buttonClick(ClickEvent event) {
            String message = input.getValue();
            sendMessage(message);
        }
    }
    private void sendMessage(String content){
        if(!Objects.equals(content, "")) {
            Message message = new Message();
            message.setUser(user);
            message.setContent(content);

            Broadcaster.broadcastMessage(message);
            input.setValue("");
        } else {
            new Notification("Message cannot be empty!").show(Page
                    .getCurrent());

        }
    }

    public void recievedMessage(Message message){
        final Label messageLabel = new Label(message.getUser().getUsername() + ": " + message.getContent());
        chatContent.addComponent(messageLabel);
        chatContentPanel.setContent(chatContent);
        chatContentPanel.setScrollTop(10000);
    }

    public void gameChanged(Game game){
//        final Label messageLabel = new Label("//" + game.toString());
//        chatContent.addComponent(messageLabel);
//        chatContentPanel.setContent(chatContent);
//        chatContentPanel.setScrollTop(10000);
    }
}
