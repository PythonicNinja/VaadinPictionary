package pl.Wojtek.ui;

import javax.servlet.annotation.WebServlet;

import pl.Wojtek.view.RegisterView;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("valo")
public class RegisterUI extends UI {
    private RegisterView registerView;

    @Override
    protected void init(VaadinRequest request) {
        registerView = new RegisterView();
        setContent(registerView);
    }

    @WebServlet(value = "/auth/register/*", asyncSupported = true)
    @VaadinServletConfiguration(widgetset = "pl.Wojtek.MyAppWidgetset", productionMode = false, ui = RegisterUI.class)
    public static class Servlet extends VaadinServlet {

    }

}
