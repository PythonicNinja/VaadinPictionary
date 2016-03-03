package pl.Wojtek.view;

import pl.Wojtek.dao.UserDao;
import pl.Wojtek.model.User;

import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

public class RegisterView extends VerticalLayout {

    public RegisterView() {
        addComponent(new FormLayoutClass());
    }

    private class FormLayoutClass extends CustomComponent {
        private VerticalLayout v;
        private Button auth;
        private Button register;
        private TextField username;
        private PasswordField password;
        private PasswordField matchingPassword;
        private User user = new User();
        private Notification n;

        public FormLayoutClass() {
            setMargin(new MarginInfo(true, true, true, true));
            auth = new Button("Login page");
            BeanItem<User> item = new BeanItem<User>(user);
            n = new Notification("Passwords do not match");
            n.setPosition(Position.TOP_CENTER);
            v = new VerticalLayout();
            v.addStyleName("loginregisterStyle");
            setCompositionRoot(v);
            register = new Button("Register");
            username = new TextField("Username: ");
            username.addValidator(new BeanValidator(User.class, "username"));
            username.addValidator(new UsernameValidator());
            username.setImmediate(true);

            password = new PasswordField("Password: ");
            password.addValidator(new BeanValidator(User.class, "password"));
            //password.addValidator(new PasswordValidator());
            password.setImmediate(true);
            matchingPassword = new PasswordField("Matching password: ");
            matchingPassword.addValidator(new BeanValidator(User.class, "password"));
            //matchingPassword.addValidator(new PasswordValidator());
            matchingPassword.setImmediate(true);

            v.addComponent(username);
            v.addComponent(password);
            v.addComponent(matchingPassword);
            v.addComponent(register);

            register.addClickListener(new Button.ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    if (username.isValid() && matchingPassword.isValid() && password.isValid()) {
                        if(matchingPassword.getValue().equals(password.getValue())){
                            user.setUsername(username.getValue());
                            user.setPassword(password.getValue());
                            new UserDao().addUser(user);
                            UI.getCurrent().getPage().setLocation("/auth/login");
                        }else{
                            n.show(Page.getCurrent());
                        }
                    }
                }
            });

            auth.addClickListener(new Button.ClickListener() {

                @Override
                public void buttonClick(ClickEvent event) {
                    UI.getCurrent().getPage().setLocation("/auth/login");
                }
            });
            v.addComponent(auth);


        }

        private class UsernameValidator implements Validator {

            @Override
            public void validate(Object value) throws InvalidValueException {
                String object = (String) value;
                if (new UserDao().getUser((String) object) != null) {
                    throw new InvalidValueException("User already exists");
                }

            }

        }

    }

}
