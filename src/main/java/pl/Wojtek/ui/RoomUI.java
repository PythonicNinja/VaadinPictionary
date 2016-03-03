package pl.Wojtek.ui;

import javax.servlet.annotation.WebServlet;

import pl.Wojtek.view.RoomView;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("valo")
public class RoomUI extends UI {
    private RoomView roomView;

    @Override
    protected void init(VaadinRequest request) {
        roomView = new RoomView(request);
        setContent(roomView);
    }

    @WebServlet(value = "/room/*", asyncSupported = true)
    @VaadinServletConfiguration(widgetset = "pl.Wojtek.MyAppWidgetset", productionMode = false, ui = RoomUI.class)
    public static class Servlet extends VaadinServlet {

    }

}
