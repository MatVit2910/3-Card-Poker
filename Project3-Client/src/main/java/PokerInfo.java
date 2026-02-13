import java.io.Serializable;
import java.util.ArrayList;

//class to pass info of game between client and server
public class PokerInfo implements Serializable {
    //data members
    private ArrayList<Card> player;
    private ArrayList<Card> dealer;
    private String playerStr;
    private String dealerStr;
    private int anteBet;
    private int playBet;
    private int winningsAmt;
    private int pairPlusAmt;
    private int pairPlus;
    private boolean fold;
    private boolean dealerQualifies;
    private boolean startNewGame;
    private boolean freshStart;

    //constructor
    public PokerInfo(){
        player = null;
        dealer = null;
        playerStr = "";
        dealerStr = "";
        anteBet = -1;
        playBet = -1;
        winningsAmt = -1;
        pairPlus = -1;
        fold = false;
        dealerQualifies = false;
        startNewGame = false;
        pairPlusAmt = -1;
        freshStart = false;
    }

    //setters
    public void setPlayerHand(ArrayList<Card> player){
        this.player = player;
    }
    public void setDealerHand(ArrayList<Card> dealer){
        this.dealer = dealer;
    }
    public void setPlayerStr(String playerStr){
        this.playerStr = playerStr;
    }
    public void setDealerStr(String dealerStr){
        this.dealerStr = dealerStr;
    }
    public void setWinningsAmt(int winningsAmt){
        this.winningsAmt = winningsAmt;
    }
    public void setPairPlus(int pairPlus){
        this.pairPlus = pairPlus;
    }
    public void setFold(boolean fold){
        this.fold = fold;
    }
    public void setAnteBet(int anteBet){
        this.anteBet = anteBet;
    }
    public void setPlayBet(int playBet){
        this.playBet = playBet;
    }
    public void setDealerQualifies(boolean dealerQualifies){
        this.dealerQualifies = dealerQualifies;
    }
    public void setStartNewGame(boolean startNewGame){
        this.startNewGame = startNewGame;
    }
    public void setPairPlusAmt(int pairPlusAmt){
        this.pairPlusAmt = pairPlusAmt;
    }
    public void setFreshStart(boolean freshStart){
        this.freshStart = freshStart;
    }

    //getters
    public ArrayList<Card> getPlayerHand(){
        return player;
    }
    public ArrayList<Card> getDealerHand(){
        return dealer;
    }
    public String getPlayerStr(){return  playerStr;}
    public String getDealerStr(){return  dealerStr;}
    public int getWinningsAmt(){
        return winningsAmt;
    }
    public int getPairPlus(){
        return pairPlus;
    }
    public boolean getFold(){
        return fold;
    }
    public int getAnteBet(){
        return anteBet;
    }
    public int getPlayBet(){
        return playBet;
    }
    public boolean getDealerQualifies(){
        return dealerQualifies;
    }
    public boolean getStartNewGame(){
        return  startNewGame;
    }
    public int getPairPlusAmt(){
        return pairPlusAmt;
    }
    public boolean getFreshStart(){
        return freshStart;
    }
}
