import java.io.Serializable;

//a class that represents a card
public class Card implements Serializable {
    //data members
    private String suit;
    private String rank;
    private String url;

    //constructor
    public Card(String suit, String rank, String url) {
        this.suit = suit;
        this.rank = rank;
        this.url = url;
    }

    //getters
    public String getSuit() {
        return suit;
    }
    public String getRank(){
        return rank;
    }
    public String getUrl(){return url;}

    //a function that transforms card ranks into integers
    //for comparison purposes
    public int getRankValue(){
        try {
            return Integer.parseInt(rank);
        }
        catch(Exception e){
            switch (rank){
                case "A":
                    return 14;
                case "J":
                    return 11;
                case "Q":
                    return 12;
                case "K":
                    return 13;
                default:
                    return -1;
            }
        }
    }

    //setters for testing purposes
    public void setSuit(String suit){
        this.suit = suit;
    }
    public void setRank(String rank){
        this.rank = rank;
    }
}
