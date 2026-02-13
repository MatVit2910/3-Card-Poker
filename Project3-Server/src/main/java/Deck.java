import java.util.ArrayList;
import java.util.Collections;

//class that represents a standard deck of cards (without jokers)
public class Deck {

    //data members
    private final ArrayList<Card> cards;

    //constructor
    public Deck(){
        cards = new ArrayList<>();
        //arr of ranks
        String[] ranks = {
               "A", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "J", "Q", "K"
        };

        //arr of suits
        String[] suits = {
                "Clubs", "Diamonds", "Hearts", "Spades"
        };

        //populate deck
        for (String suit : suits) {
            for (String rank : ranks) {
                String url = "/cards/" + rank + suit.charAt(0) + ".png";
                Card card = new Card(suit, rank, url);
                cards.add(card);
            }
        }
    }

    //a function to deal hands
    //it returns an array of 3 cards
    public ArrayList<Card> dealHand(){
        ArrayList<Card> hand = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            hand.add(cards.get(0));
            cards.remove(cards.get(0));
        }
        return hand;
    }

    //getters and wrapper of shuffle
    public void shuffle(){
        Collections.shuffle(cards);
    }
    public  ArrayList<Card> getCards(){
        return cards;
    }

}
