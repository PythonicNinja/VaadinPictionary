package pl.Wojtek.ui;

import javax.servlet.annotation.WebServlet;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.components.colorpicker.ColorChangeEvent;
import com.vaadin.ui.components.colorpicker.ColorChangeListener;
import org.vaadin.hezamu.canvas.Canvas;
import pl.Wojtek.model.Message;
import pl.Wojtek.model.User;
import pl.Wojtek.util.Broadcaster;
import pl.Wojtek.view.ChatView;


@Push
@SuppressWarnings("serial")
@Theme("valo")
@Widgetset("pl.Wojtek.MyAppWidgetset")
public class GameUI extends UI implements Broadcaster.BroadcastListener {

    private User user;
    private Canvas canvas;
    private ChatView chatView;
    private Boolean isClicked = false;
    private int prevX = -1;
    private int prevY = -1;
    private String strokeColor = "black";
    private double strokeSize = 5.0;

    @Override
    public void receiveBroadcast(Message message) {

    }

    @WebServlet(value = { "/VAADIN/*", "/game/*" }, asyncSupported = true)
    @VaadinServletConfiguration(widgetset="pl.Wojtek.MyAppWidgetset", productionMode = false, ui = GameUI.class)
    public static class Servlet extends VaadinServlet {
    }


    @Override
    protected void init(VaadinRequest request) {
        user = ((User) VaadinSession.getCurrent().getSession().getAttribute("user"));

        VerticalLayout content = new VerticalLayout();
        setContent(content);

        final HorizontalLayout firstRow = new HorizontalLayout();
        firstRow.setSizeFull();
        content.addComponent(firstRow);

        VerticalLayout canvasWrapper = new VerticalLayout();
        canvasWrapper.setSizeFull();
        canvas = new Canvas();
        canvas.setSizeFull();
        canvas.setHeight("500px");
        canvasWrapper.addComponent(canvas);
        final HorizontalLayout bs = new HorizontalLayout();
        final HorizontalLayout bs2 = new HorizontalLayout();
        canvasWrapper.addComponent(bs);
        canvasWrapper.addComponent(bs2);

        firstRow.addComponent(canvasWrapper);

        chatView = new ChatView();
        firstRow.addComponent(chatView);

        canvas.addMouseMoveListener(new Canvas.CanvasMouseMoveListener() {
            @Override
            public void onMove(MouseEventDetails mouseDetails) {
                int x = mouseDetails.getClientX();
                int y = mouseDetails.getClientY();

//                System.out.println("Mouse moved at "
//                        + x + ","
//                        + y + " - " + strokeColor);

                if(isClicked && prevX != -1 && prevY != -1) {
                    canvas.saveContext();
                    canvas.beginPath();

                    canvas.setLineWidth(strokeSize);
                    canvas.setLineCap("round");
                    canvas.setMiterLimit(1);
                    canvas.setStrokeStyle(strokeColor);
                    canvas.moveTo(prevX, prevY);
                    canvas.lineTo(x, y);

                    canvas.stroke();
                    canvas.closePath();

                    canvas.restoreContext();
                }
                prevX = x;
                prevY = y;

            }
        });

        canvas.addMouseDownListener(new Canvas.CanvasMouseDownListener(){
            @Override
            public void onMouseDown() {
                isClicked = true;
            }
        });

        canvas.addMouseUpListener(new Canvas.CanvasMouseUpListener(){
            @Override
            public void onMouseUp() {
                isClicked = false;
            }
        });



        bs.addComponent(new Button("Clear", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                canvas.clear();
            }
        }));

        bs.addComponent(this.getColorPicker());

        bs.addComponent(this.getSlider());

        bs2.addComponent(new Button("Logout", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                user.logout();
                getUI().getPage().reload();
            }
        }));


        canvas.loadImages(new String[] {
                "http://webapp.org.ua/wp-content/uploads/2011/10/gwtlogo.jpg",
                "http://upload.wikimedia.org/wikipedia/commons/3/38/HTML5_Logo.svg",
                "http://jole.virtuallypreinstalled.com/paymate/img/vaadin-logo.png" });

    }

    private Slider getSlider(){
        Slider slider = new Slider();
        slider.setImmediate(true);
        slider.setMin(0);
        slider.setMax(100.0);
        slider.setValue(strokeSize);
        slider.addValueChangeListener(e -> {
            strokeSize = (Double) e.getProperty().getValue();

            Notification.show("Value changed:",
                    String.valueOf(strokeSize),
                    Notification.Type.TRAY_NOTIFICATION);
        });

        return slider;
    }

    private ColorPicker getColorPicker(){
        ColorPicker picker = new ColorPicker();
        picker.addColorChangeListener(new ColorChangeListener() {
            @Override
            public void colorChanged(ColorChangeEvent colorChangeEvent) {
                strokeColor = colorChangeEvent.getColor().getCSS();

                Notification.show("Color changed: " + strokeColor,
                        Notification.Type.TRAY_NOTIFICATION);

            }
        });
        picker.setSwatchesVisibility(false);
        picker.setHistoryVisibility(false);
        picker.setTextfieldVisibility(false);
        picker.setHSVVisibility(false);
        return picker;
    }

    private void updateChat(Message message){

    }

}