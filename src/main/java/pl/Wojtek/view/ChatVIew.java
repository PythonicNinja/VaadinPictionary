package pl.Wojtek.view;

import com.vaadin.ui.*;
import pl.Wojtek.model.Message;
import pl.Wojtek.model.User;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button.ClickEvent;
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
        chatContentPanel = new Panel();
        chatContent = new VerticalLayout();
        chatControl = new HorizontalLayout();
        chatContentPanel.setHeight("500px");
        chatContentPanel.setContent(chatContent);
        input = new TextField();
        input.setWidth("100%");
        chatControl.addComponent(input);
        chatControl.setSizeFull();
        Button sendButton = new Button("Send");
        sendButton.setClickShortcut(KeyCode.ENTER);
        sendButton.addClickListener(new SendMessageButton());
        chatControl.addComponent(sendButton);

        addComponent(chatContentPanel);
        addComponent(chatContent);
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

        chatContent.addComponent(
            new Label("-" + message.getUser().getUsername() + ": " + message.getContent())
        );

    }
}
