import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

//class for the poker logic (basically PokerLogic)
public class Game {
    //data members
    private final Deck deck;
    private ArrayList<Card> dealerHand;
    private ArrayList<Card> playerHand;
    private boolean dealerQualifies;

    //constructor
    public Game() {
        deck = new Deck();
    }

    //a function to start the 3 card poker game
    //it just shuffles the deck and deals the hands
    public void startGame(){
        deck.shuffle();
        playerHand = deck.dealHand();
        dealerHand = deck.dealHand();
    }

    // hashmap to rank each hand
    private final Map<String, Integer> handsOrder = Map.of(
            "Straight Flush", 6,
            "Three of a Kind", 5,
            "Straight", 4,
            "Flush", 3,
            "Pair", 2,
            "High Card", 1
    );

    //a function to get the sorted rank values of a hand of cards
    //it returns an array of ints with the sorted ranks
    private ArrayList<Integer> getSortedRanks(ArrayList<Card> hand){
        ArrayList<Integer> ranks = new ArrayList<>();
        for (Card c : hand){
            ranks.add(c.getRankValue());
        }
        Collections.sort(ranks);
        return ranks;
    }

    //a function to evaluate a poker hand
    //it returns a string representing the hand
    public String evaluateHand(ArrayList<Card> hand){

        ArrayList<Integer> ranks = getSortedRanks(hand);

        /*-----------------------------------
        Hands:
        Straight Flush - Consecutive ranks and same suit
        3 Kind - 3 cards same rank
        Straight - Consecutive ranks (no same suit)
        Flush - 3 cards same suit
        Pair - 2 cards same rank
        -----------------------------------*/

        //ugly booleans but they work :)
        boolean isFlush = hand.get(0).getSuit().equals(hand.get(1).getSuit())
                && hand.get(1).getSuit().equals(hand.get(2).getSuit());
        boolean isStraight = (ranks.get(0) == ranks.get(1) - 1) && (ranks.get(1) == ranks.get(2) - 1)
                || (ranks.get(0) == 2 && ranks.get(1) == 3 && ranks.get(2) == 14);
        boolean isThreeOfAKind = (ranks.get(0).equals(ranks.get(1)) && ranks.get(1).equals(ranks.get(2)));
        boolean isPair = !isThreeOfAKind &&
                (ranks.get(0).equals(ranks.get(1)) || ranks.get(1).equals(ranks.get(2)) || ranks.get(0).equals(ranks.get(2)));

        //check and return hands
        if (isStraight && isFlush) {
            return "Straight Flush";
        }
        if (isThreeOfAKind) {
            return "Three of a Kind";
        }
        if (isStraight) {
            return "Straight";
        }
        if (isFlush) {
            return "Flush";
        }
        if (isPair) {
            return "Pair";
        }
        return "High Card";
    }

    //a function to calculate pair plus winnings
    //returns an int representing the winnings amt
    public int calculatePairPlus(ArrayList<Card> playerHand, int bet){
        String hand = evaluateHand(playerHand);
        switch (hand){
            case "Straight Flush":
                return 40*bet + bet;
            case "Three of a Kind":
                return 30*bet + bet;
            case "Straight":
                return 6*bet + bet;
            case "Flush":
                return 3*bet + bet;
            case "Pair":
                return 2*bet;
            default:
                return 0;
        }
    }

    //a function to compare hands and calculate winnings
    //returns an int representing the winnings amt
    public int compareHands(ArrayList<Card> dealer, ArrayList<Card> player, int anteBet, int playBet){

        dealerQualifies = false;
        String dealerHand = evaluateHand(dealer);

        //check if dealer qualifies
        if (handsOrder.get(dealerHand) >= handsOrder.get("Pair")){
            dealerQualifies = true;
        }
        else{
            for (Card c : dealer){
                if (c.getRank().equals("Q") || c.getRank().equals("K") || c.getRank().equals("A")){
                    dealerQualifies = true;
                    break;
                }
            }
        }

        //print statements are not used, they're just for debugging :)
        if (!dealerQualifies){
            System.out.println("Dealer doesn't qualify");
            return (anteBet+playBet);
        }
        String playerHand = evaluateHand(player);
        System.out.println("Player:" + playerHand);
        System.out.println("Dealer:" + dealerHand);

        Integer dealerScore = handsOrder.get(dealerHand);
        Integer playerScore = handsOrder.get(playerHand);

        //check who wins
        if (dealerScore > playerScore){
            System.out.println("Player lost :c");
            return 0;
        }
        else if (dealerScore < playerScore){
            System.out.println("Player won :D");
            return (anteBet+playBet)*2;
        }
        else{
            ArrayList<Integer> playerRanks = getSortedRanks(player);
            ArrayList<Integer> dealerRanks = getSortedRanks(dealer);
            for (int i = 2; i >= 0; i--) {
                int playerCard = playerRanks.get(i);
                int dealerCard = dealerRanks.get(i);

                if (playerCard > dealerCard) {
                    System.out.println("Player won :D");
                    return (anteBet+playBet)*2;
                } else if (playerCard < dealerCard) {
                    System.out.println("Player lost :c");
                    return 0;
                }
            }

            //no way this happens
            System.out.println("Hands are identical :O.");
            return (anteBet+playBet);
        }

    }

    //getters
    public ArrayList<Card> getPlayerHand() {
        return playerHand;
    }
    public ArrayList<Card> getDealerHand() {
        return dealerHand;
    }
    public boolean dealerQualifies() {
        return dealerQualifies;
    }

}
