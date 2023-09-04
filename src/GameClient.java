import javax.swing.*;
import java.util.Scanner;

public class GameClient {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        boolean playAgain = true;
        int bankroll = 10000;
        int bet = 0;

        while (playAgain) {
            Deck deck = new Deck();
            deck.shuffle();
            Hand playerHand = new Hand();
            Hand dealerHand = new Hand();

            playerHand.addCard(new Card("Hearts", "10"));
            dealerHand.addCard(deck.drawCard());
            playerHand.addCard(new Card("Diamonds", "10"));
            dealerHand.addCard(deck.drawCard());

            Thread.sleep(500);
            System.out.println("\nWelcome to Blackjack!");
            Thread.sleep(500);
            System.out.println("Current Balance is at $" + bankroll);
            Thread.sleep(500);
            System.out.println("**Keep in mind, betting more than half of your total bankroll means you cannot double down or split**");
            System.out.print("How much would you like to bet this round? Minimum 500: ");
            bet = getInt(sc, bankroll);

            boolean canDoubleDown = (playerHand.getCardAmount() == 2) && (bet * 2 <= bankroll);

            String str = "*Dealer is dealing the cards...*";
            for (char c : str.toCharArray()) {
                System.out.print(c);
                Thread.sleep(75);
            }
            System.out.println();
            Thread.sleep(2000);
            System.out.println("\nYour hand: ");
            Thread.sleep(500);
            playerHand.displayHand();
            System.out.println("Your hand value: " + playerHand.getHandValue());
            Thread.sleep(500);
            System.out.print("\nDealer's face up card: ");
            Thread.sleep(500);
            dealerHand.displayDealerCard();

            boolean doubledDown = false;

            if (playerHand.getHandValue() == 21) {
                System.out.println("Blackjack! You win!");
            } else {
                outerLoop:
                while (playerHand.getHandValue() < 21 && !doubledDown) {
                    System.out.print("Do you want to hit, stand, double down(enter \"dd\"), or split? ");
                    String choice = sc.nextLine().toLowerCase();

                    if ("hit".equals(choice)) {
                        playerHand.addCard(deck.drawCard());
                        System.out.println("Your hand:");
//                        Thread.sleep(500);
                        playerHand.displayHand();
                        System.out.println("Your hand value: " + playerHand.getHandValue());
                    } else if ("stand".equals(choice)) {
                        break;
                    } else if ("dd".equals(choice) || "double down".equals(choice)) {
                        if (canDoubleDown) {
                            bet *= 2;
                            playerHand.addCard(deck.drawCard());
                            doubledDown = true;
                        } else {
                            System.out.println("Cannot double down. Pick another choice.");
                        }
                    } else if ("split".equals(choice)) {
                        if (playerHand.canSplit(bet, bankroll)) {
                            // Create a new hand for the second split hand.
                            Hand splitHand = new Hand();

                            // Move one card from the original hand to the new hand.
                            Card splitCard = playerHand.removeCard(); // Implement a method to remove a card from the hand.
                            splitHand.addCard(splitCard);

                            // Draw new cards for both hands.
                            playerHand.addCard(deck.drawCard());
                            splitHand.addCard(deck.drawCard());

                            // Play the original hand (playerHand).
                            System.out.println("Your original hand:");
//                            Thread.sleep(500);
                            playerHand.displayHand();
                            System.out.println("Your hand value: " + playerHand.getHandValue());

                            // Continue the game for the original hand.
                            while (playerHand.getHandValue() < 21) {
                                System.out.print("Do you want to hit or stand? ");
                                choice = sc.nextLine().toLowerCase();

                                if ("hit".equals(choice)) {
                                    playerHand.addCard(deck.drawCard());
                                    System.out.println("Your original hand:");
//                                    Thread.sleep(500);
                                    playerHand.displayHand();
                                    System.out.println("Your hand value: " + playerHand.getHandValue());
                                } else if ("stand".equals(choice)) {
                                    break;
                                }
                            }

                            // Play the split hand (splitHand).
                            System.out.println("\nYour split hand:");
//                            Thread.sleep(500);
                            splitHand.displayHand();
                            System.out.println("Split hand value: " + splitHand.getHandValue());

                            while (splitHand.getHandValue() < 21) {
                                System.out.print("Do you want to hit or stand for your split hand? ");
                                choice = sc.nextLine().toLowerCase();

                                if ("hit".equals(choice)) {
                                    splitHand.addCard(deck.drawCard());
                                    System.out.println("Your split hand:");
//                                    Thread.sleep(500);
                                    splitHand.displayHand();
                                    System.out.println("Split hand value: " + splitHand.getHandValue());
                                } else if ("stand".equals(choice)) {
                                    break outerLoop;
                                }
                            }

                            // Evaluate the outcomes for both hands (playerHand and splitHand).
                            // Update the player's bankroll based on the results.
                            // Compare each hand's value with the dealer's hand and determine the winner for each hand.
                            // Update the bankroll based on the results.
                            // Consider ties, busts, and wins for both hands.

                            // Continue the game by asking if the player wants to play again.
                        } else {
                            System.out.println("You can't split with these cards/Do not have enough in the bankroll.");
                        }
                    }

                }
                while (dealerHand.getHandValue() < 17) {
                    dealerHand.addCard(deck.drawCard());
                }

//                Thread.sleep(500);
                System.out.println("Dealer's hand:");
//                Thread.sleep(500);
                dealerHand.displayHand();
                System.out.println("Dealer's hand value: " + dealerHand.getHandValue());

                if (playerHand.getHandValue() > 21) {
//                    Thread.sleep(500);
                    System.out.println("You bust! Dealer wins.");
                } else if (dealerHand.getHandValue() > 21 || playerHand.getHandValue() > dealerHand.getHandValue()) {
//                    Thread.sleep(500);
                    System.out.println("You win!");
                } else if (playerHand.getHandValue() == dealerHand.getHandValue()) {
//                    Thread.sleep(500);
                    System.out.println("It's a tie!");
                } else {
//                    Thread.sleep(500);
                    System.out.println("Dealer wins.");
                }
            }

            Thread.sleep(500);
            System.out.print("Do you want to play again? (yes/no): ");
            String playAgainChoice = sc.nextLine().toLowerCase();

            if (!playAgainChoice.equals("yes")) {
                playAgain = false;
                System.out.println("Thank you for playing!");
            }
        }
    }

    private static int getInt(Scanner sc, int bankroll) {
        int userInput;

        do {
            while (!sc.hasNextInt()) {
                System.out.print("Sorry, your input must be a numerical value! Try again: ");
                sc.next();
            }
            userInput = sc.nextInt();

            if (userInput < 500 || userInput > bankroll) {
                if (userInput < 500) {
                    System.out.print("Your bet is below the minimum! Try again: ");
                } else {
                    System.out.print("You do not have sufficient funds in your account for this bet. Try again: ");
                }
            }
        } while (userInput < 500 || userInput > bankroll);

        return userInput;
    }
}
