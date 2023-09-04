public class Card {
    private final String suit;
    public final String rank;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }
    public String toString() {
        return rank + " of " + suit;
    }

}
