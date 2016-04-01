package pl.Wojtek.model;

import pl.Wojtek.dao.WordDao;
import pl.Wojtek.util.Broadcaster;
import pl.Wojtek.util.State;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

/**
 *
 */
public class Game {
    private Room room;
    private Player gameMaster;
    private LinkedHashSet<Player> players = new LinkedHashSet<Player>();
    private State state;
    private Iterator<Player> playerIterator;
    private Player currentPlayer;
    private Word currentWord;
    private List<Word> words = new LinkedList<Word>();
    private Iterator<Word> wordIterator;
    private int timeLeft = 0;


    @Min(value = 10)   // 10 seconds
    @Max(value = 3*60) // 3 minutes
    private int turnTime = 60;

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Player getCurrentPlayer() {return currentPlayer;}

    public void setCurrentPlayer(Player currentPlayer) {this.currentPlayer = currentPlayer;}

    public Word getCurrentWord() {return currentWord;}

    public void setCurrentWord(Word currentWord) {this.currentWord = currentWord;}

    public LinkedHashSet<Player> getPlayers() {
        return players;
    }

    public void setPlayers(LinkedHashSet<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player){
        if(!this.players.contains(player))
            this.players.add(player);
    }

    public void removePlayer(Player player){
        this.players.remove(player);
    }

    public int getTurnTime() {
        return turnTime;
    }

    public void setTurnTime(int turnTime) {
        this.turnTime = turnTime;
    }


    public Player getGameMaster() {
        return gameMaster;
    }

    public void setGameMaster(Player gameMaster) {
        this.gameMaster = gameMaster;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void sync(){
        Broadcaster.broadcastMessage(this);
    }

    public Player nextPlayer(){
        if (this.playerIterator==null || !this.playerIterator.hasNext())
            this.playerIterator = this.players.iterator();

        return this.playerIterator.next();
    }

    public Word nextWord(){
        if(this.wordIterator==null)
            this.wordIterator = this.words.iterator();

        if (!this.wordIterator.hasNext())
            return null;

        return this.wordIterator.next();
    }

    public void getTurn(){
        if(this.timeLeft==0) {
            if (this.currentPlayer == null)
                this.words = new WordDao().getWords();

            this.currentWord = nextWord();
            if (this.currentWord == null) {
                this.setState(new State("End"));
                this.sync();
            }

            this.currentPlayer = nextPlayer();
            this.timeLeft = this.turnTime;
        }
    }

    public void reset(){
        this.setPlayers(new LinkedHashSet<Player>());
        this.currentPlayer = null;
        this.timeLeft = 0;
        this.wordIterator=null;
        this.setGameMaster(null);
        this.setState(new State("Waiting for users"));
    }

    @Override
    public String toString() {
        return "Game(\n" + this.hashCode() +
                "Room: " + this.getRoom() + "\n" +
                "GameMaster: " + this.getGameMaster() + "\n" +
                "State: " + this.getState() + "/n" +
                "Players: " + this.getPlayers() + "\n" +
                "Current player: " + this.currentPlayer + "\n" +
                "Word: " + this.currentWord + "\n" +
                "Words: " + this.words + "\n" +
                "TimeLeft: " + this.timeLeft + "\n" +
                "Winner: " + this.getWinner() + "\n" +
                ")";
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void decreaseTimeLeft() {
        this.timeLeft = 0;
        this.sync();
    }

    public boolean hasWord() {
        return this.currentWord != null;
    }

    public void guessed(User user, Long timeLeft) {
        for (Player player: players) {
            System.out.print("tileLeft" + timeLeft);
            if(player.getUsername().equals(user.getUsername())){
                player.addPoints(timeLeft/100.0+100.0);
            }
            if(player.getUsername().equals(this.currentPlayer.getUsername())){
                player.addPoints(timeLeft/100.0);
            }
        }
        this.timeLeft = 0;
        this.sync();
    }

    public Player getWinner(){
        Player winner = null;
        for (Player player: players) {
            if(winner == null || player.getPoints() > winner.getPoints()){
                winner = player;
            }
        }
        return winner;
    }
}
