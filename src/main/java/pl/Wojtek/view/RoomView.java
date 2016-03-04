package pl.Wojtek.view;

import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import pl.Wojtek.dao.RoomDao;
import pl.Wojtek.dao.UserDao;
import pl.Wojtek.model.Room;
import pl.Wojtek.model.User;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class RoomView extends VerticalLayout {

    public RoomView(VaadinRequest request) {
        FormLayoutClass form_layout = new FormLayoutClass(request);
        addComponent(form_layout);
    }

    private class FormLayoutClass extends CustomComponent {
        public VerticalLayout v;
        private Button register;
        private TextField name;
        private Room room = new Room();

        public FormLayoutClass(VaadinRequest request) {

            BeanItem<Room> item = new BeanItem<Room>(room);

            v = new VerticalLayout();
            v.addStyleName("loginregisterStyle");
            setCompositionRoot(v);

            register = new Button("Create");

            name = new TextField("Room name: ");
            name.addValidator(new BeanValidator(Room.class, "name"));
            name.setImmediate(true);

            v.addComponent(name);
            v.addComponent(register);

            register.addClickListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    if (name.isValid()) {
                        room.setName(name.getValue());
                        new RoomDao().addRoom(room);

                        User user = (User) ((HttpServletRequest) request).getSession().getAttribute("user");
                        user.setRoom(room);
                        UI.getCurrent().getPage().setLocation("/game");
                    }
                }
            });
        }
    }

}
