# 3-Card Poker

A simple implementation of the popular casino game **Three Card Poker**. This project allows users to play against a dealer, place bets, and track their winnings. Done in Java using the Client-Server Model.

## 🃏 How to Play

The goal is to beat the dealer with a better 3-card hand.

### Hand Rankings (Highest to Lowest)
1.  **Straight Flush**: Three consecutive cards of the same suit.
2.  **Three of a Kind**: Three cards of the same rank.
3.  **Straight**: Three consecutive cards of mixed suits.
4.  **Flush**: Three cards of the same suit.
5.  **Pair**: Two cards of the same rank.
6.  **High Card**: None of the above (highest card wins).

### Game Rules
1.  **Ante**: The player places an initial bet (Ante) to receive cards.
2.  **The Deal**: Both the player and dealer receive 3 cards. The player's cards are face up; the dealer's cards remain hidden.
3.  **Fold or Play**:
    * **Fold**: You forfeit your Ante bet and the hand ends.
    * **Play**: You place a second bet (equal to the Ante) to compare hands with the dealer.
4.  **Dealer Qualifies**: The dealer needs at least a **Queen High** to qualify.
    * If the dealer *does not* qualify, the Play bet pushes (returns) and the Ante pays 1:1.
    * If the dealer *does* qualify, the hands are compared.
        * **Player Wins**: Both Ante and Play bets pay 1:1.
        * **Dealer Wins**: Player loses both bets.
        * **Tie**: Both bets push.

<img width="933" height="663" alt="image" src="https://github.com/user-attachments/assets/ac995913-6be9-42fe-8b3f-7626bfa0f5bd" />
