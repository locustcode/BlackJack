import java.util.ArrayList;
public class Hand {
    private final ArrayList<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public Card removeCard() {
        return cards.remove(cards.size() - 1);
    }

    public Card getLastCard() {
        return cards.get(cards.size() - 1);
    }
    public int getCardAmount() {
        return cards.size();
    }

    public boolean canSplit(int bet, int bankroll) {
        return cards.size() == 2 && cards.get(0).rank.equals(cards.get(1).rank) && bet * 2 <= bankroll;
    }

    public boolean blackJack(Hand hand) {
        return hand.getHandValue() == 21 && hand.getCardAmount() == 2;
    }

    public int getDealerFirstCardValue() {
        Card firstCard = cards.get(0);
        String rank = firstCard.toString().split(" ")[0];
        int value;

        if ("Ace".equals(rank)) {
            value = 11;
        } else if ("King".equals(rank) || "Queen".equals(rank) || "Jack".equals(rank)) {
            value = 10;
        } else {
            value = Integer.parseInt(rank);
        }

        return value;
    }


    public int getHandValue() {
        int value = 0;
        int numAces = 0;

        for (Card card : cards) {
            String rank = card.toString().split(" ")[0];
            if ("Ace".equals(rank)) {
                numAces++;
                value += 11;
            } else if ("King".equals(rank) || "Queen".equals(rank) || "Jack".equals(rank)) {
                value += 10;
            } else {
                value += Integer.parseInt(rank);
            }
        }

        while (numAces > 0 && value > 21) {
            value -= 10;
            numAces--;
        }

        return value;
    }

    public String displayDealerCard() {
        return cards.get(0).toString();
    }

    public String displayHand() {
        StringBuilder handString = new StringBuilder();
        for (Card card : cards) {
            handString.append(card.toString()).append(" ").append("\n");
        }
        return handString.toString();
    }
}
