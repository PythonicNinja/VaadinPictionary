package pl.Wojtek.ui;

import javax.servlet.annotation.WebServlet;

import pl.Wojtek.view.LoginView;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Theme("valo")
public class LoginUI extends UI {
    private LoginView loginView;

    @Override
    protected void init(VaadinRequest request) {
        loginView = new LoginView();
        setContent(loginView);
    }

    @WebServlet(value = "/auth/login/*", asyncSupported = true)
    @VaadinServletConfiguration(widgetset = "pl.Wojtek.MyAppWidgetset", productionMode = false, ui = LoginUI.class)
    public static class Servlet extends VaadinServlet {

    }

}
