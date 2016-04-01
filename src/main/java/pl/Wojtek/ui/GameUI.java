package pl.Wojtek.ui;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.components.colorpicker.ColorChangeEvent;
import com.vaadin.ui.components.colorpicker.ColorChangeListener;
import org.vaadin.hezamu.canvas.Canvas;
import org.vaadin.kim.countdownclock.CountdownClock;
import pl.Wojtek.dao.GameDao;
import pl.Wojtek.model.*;
import pl.Wojtek.util.Broadcaster;
import pl.Wojtek.util.Point;
import pl.Wojtek.util.State;
import pl.Wojtek.view.ChatView;

import javax.servlet.annotation.WebServlet;
import java.util.Calendar;
import java.util.Iterator;


@Push
@SuppressWarnings("serial")
@Theme("valo")
@Widgetset("pl.Wojtek.MyAppWidgetset")
public class GameUI extends UI implements Broadcaster.BroadcastListener {

    private User user;
    private Canvas canvas;
    private ChatView chatView;
    private Button ready;
    private Button startBtn;
    private HorizontalLayout gameOptions;
    private HorizontalLayout canvasControl;
    private Boolean isClicked = false;
    private double prevX = -1;
    private double prevY = -1;
    private String strokeColor = "black";
    private double strokeSize = 5.0;
    private CountdownClock countdownClock;

    @Override
    public void receiveBroadcast(Message message) {
        access(new Runnable() {
            @Override
            public void run() {
                checkGuessed(message);
                chatView.recievedMessage(message);
            }
        });
    }

    @Override
    public void receiveBroadcast(Draw draw) {
        access(new Runnable() {
            @Override
            public void run() {
                GameUI.updateDrawing(canvas, draw);
            }
        });
    }

    @Override
    public void receiveBroadcast(Clear clear) {
        access(new Runnable() {
            @Override
            public void run() {
                GameUI.updateDrawing(canvas, clear);
            }
        });
    }

    @Override
    public void receiveBroadcast(Game game) {
        access(new Runnable() {
            @Override
            public void run() {
                chatView.gameChanged(game);
                updateGame(game);
            }
        });
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
        canvas.setHeight("600px");
        canvasWrapper.addComponent(canvas);
        canvasControl = new HorizontalLayout();
        gameOptions = new HorizontalLayout();
        canvasWrapper.addComponent(canvasControl);
        canvasWrapper.addComponent(gameOptions);
        firstRow.addComponent(canvasWrapper);

        chatView = new ChatView();
        firstRow.addComponent(chatView);

        Broadcaster.register(user.getRoom(), this);

        canvas.addMouseMoveListener(new Canvas.CanvasMouseMoveListener() {
            @Override
            public void onMove(MouseEventDetails mouseDetails) {
                double x = mouseDetails.getClientX();
                double y = mouseDetails.getClientY();


                if(isClicked && prevX != -1 && prevY != -1) {

                    Draw draw = new Draw();
                    draw.setUser(user);
                    draw.setColor(strokeColor);
                    draw.setNewPoint(new Point(x, y));
                    draw.setPrevPoint(new Point(prevX, prevY));
                    draw.setStrokeSize(strokeSize);

                    Broadcaster.broadcastMessage(draw);
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

        canvasControl.addComponent(new Button("Clear", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Broadcaster.broadcastMessage(new Clear(user));
            }
        }));

        canvasControl.addComponent(this.getColorPicker());
        canvasControl.addComponent(this.getSlider());

        countdownClock = new CountdownClock();
        canvasControl.addComponent(countdownClock);

        gameOptions.addComponent(new Button("Logout", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                user.logout();
                getUI().getPage().reload();
            }
        }));


        gameOptions.addComponent(new Button("Leave room", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                user.setRoom(null);
                getUI().getPage().reload();
            }
        }));

        Button ready = initializeReady();
        gameOptions.addComponent(ready);


        Room room = user.getRoom();
        Game game = room.getGame();
        if(game!=null)
            game.sync();
    }

    private void checkGuessed(Message message) {
        if(user.getUsername().equals(message.getUser().getUsername())) {
            Room room = user.getRoom();
            Game game = new GameDao().getGame(room);

            if (game != null && game.hasWord()) {
                Word word = game.getCurrentWord();
                if (word.getWord().equals(message.getContent())) {

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(countdownClock.getDate());
                    long timeLeft = calendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();

                    game.guessed(message.getUser(), timeLeft);
                }
            }
        }
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

    private static void updateDrawing(Canvas canvas, Draw draw){

        canvas.saveContext();
        canvas.beginPath();

        canvas.setLineWidth(draw.getStrokeSize());
        canvas.setLineCap("round");
        canvas.setMiterLimit(1);
        canvas.setStrokeStyle(draw.getColor());
        canvas.moveTo(draw.getPrevPoint().getX(), draw.getPrevPoint().getY());
        canvas.lineTo(draw.getNewPoint().getX(), draw.getNewPoint().getY());

        canvas.stroke();
        canvas.closePath();

        canvas.restoreContext();
    }

    private static void updateDrawing(Canvas canvas, Clear clear){
        canvas.clear();
    }


    private void updateGame(Game game){

        String gameState = game.getState().getName();

        if(gameState.equals("Waiting for users")){
            Boolean alreadyReady = false;
            for (Player player: game.getPlayers()) {
                if(user.getUsername().equals(player.getUsername())){
                    alreadyReady = true;
                    break;
                }
            }
            if(!alreadyReady) {
                canvasControl.removeComponent(countdownClock);
                gameOptions.removeComponent(ready);
                gameOptions.addComponent(initializeReady());
            }

            int amountPlayers = game.getPlayers().size();

            if(amountPlayers == 1){
                Iterator<Player> it = game.getPlayers().iterator();
                if(it.hasNext())
                    game.setGameMaster(it.next());
            }
            if(amountPlayers >= 2 && game.getGameMaster().getUsername().equals(user.getUsername())){
                gameOptions.addComponent(initializeStart());
            }

        }
        else if(gameState.equals("Started")){
            if(game.getGameMaster().getUsername().equals(user.getUsername())) {
                game.getTurn();
            }

            canvasControl.removeComponent(countdownClock);

            if (game.getTurnTime() == game.getTimeLeft()) {

                if (user.getUsername().equals(game.getCurrentPlayer().getUsername()) && game.getCurrentWord() != null) {
                    Notification.show("Current word: " + game.getCurrentWord().getWord(),
                            Notification.Type.WARNING_MESSAGE);
                    Broadcaster.broadcastMessage(new Clear(user));
                }

                Calendar c = Calendar.getInstance();
                c.add(Calendar.SECOND, game.getTurnTime());
                countdownClock = new CountdownClock();
                countdownClock.setDate(c.getTime());
                countdownClock.setFormat("<span style='font: 15px Arial; margin-top: 15px; margin-left: 5px'>"
                        + "Remaining %s.%ts</span>");
                countdownClock.addEndEventListener(new CountdownClock.EndEventListener() {
                    public void countDownEnded(CountdownClock clock) {
                        if (user.getUsername().equals(game.getCurrentPlayer().getUsername()) && game.getCurrentWord() != null) {
                            game.decreaseTimeLeft();
                        }
                    }
                });
                canvasControl.addComponent(countdownClock);
            }

        }else if(gameState.equals("End")){

            canvasControl.removeComponent(countdownClock);
            gameOptions.removeComponent(ready);
            gameOptions.addComponent(initializeReady());

            Player winner = game.getWinner();
            if(winner.getUsername().equals(user.getUsername())){
                game.reset();
                game.sync();
            }
        }
    }

    private Button initializeReady(){
        ready = new Button("Ready to play", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Button btn = event.getButton();
                btn.setCaption("Waiting for users");

                Room room = user.getRoom();
                Game game = new GameDao().getGame(room);

                if(game == null){
                    game = new Game();

                    Player player = new Player(user);
                    game.addPlayer(player);
                    game.setGameMaster(player);
                    game.setState(new State("Waiting for users"));
                    new GameDao().addGame(game);
                }else{
                    Player player = new Player(user);
                    game.addPlayer(player);
                }

                room.setGame(game);
                game.setRoom(room);
                game.sync();
            }
        });
        ready.setDisableOnClick(true);
        return ready;
    }

    public Button initializeStart(){
        startBtn = new Button("Start Game");
        startBtn.setDisableOnClick(true);
        startBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                Button btn = event.getButton();
                btn.setCaption("Started");

                Room room = user.getRoom();
                Game game = new GameDao().getGame(room);

                game.setState(new State("Started"));
                game.sync();
                gameOptions.removeComponent(startBtn);
            }
        });
        return startBtn;
    }

    @Override
    public void detach() {
        Broadcaster.unregister(user.getRoom(), this);
        super.detach();
    }

}