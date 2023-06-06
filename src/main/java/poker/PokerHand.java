package poker;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PokerHand {

    private final List<Card> cards;

    public PokerHand(List<String> cards) {
        this.cards = cards.stream().map(Card::parse).toList();
    }

    /**
     * Any five cards of the same suit?
     */
    public boolean hasFlush() {
        return this.cards.stream().map(Card::suit).distinct().count() == 1;
    }

    /**
     * Two cards of the same rank?
     */
    public boolean hasPair() {
        return getCardCounts().values().contains(2L);
    }

    /**
     * Three cards of the same rank?
     */
    public boolean hasThreeOfAKind() {
        return getCardCounts().values().contains(3L);
    }

    /**
     * Two different pairs?
     */
    public boolean hasTwoPairs() {
        return Collections.frequency(getCardCounts().values(), 2L) == 2;
    }

    /**
     * Four cards of the same rank?
     */
    public boolean hasFourOfAKind() {
        return getCardCounts().values().contains(4L);
    }

    /**
     * Three of a kind with a pair?
     */
    public boolean hasFullHouse() {
        return hasPair() && hasThreeOfAKind();
    }

    /**
     * Five cards in a sequence? Note that an ace can be either 1 or 14!
     */
    public boolean hasStraight() {
        List<Integer> values = getCardValues();

        // has to have five distinct numbers
        if (values.stream().distinct().count() != 5) {
            return false;
        }

        var min = Collections.min(values);
        var max = Collections.max(values);
        var straight = min == max - 4;

        return straight || hasRroyalStraight();
    }

    private boolean hasRroyalStraight() {
        return getCardValues().containsAll(List.of(1, 10, 11, 12, 13));
    }

    private List<Integer> getCardValues() {
        return cards.stream().map(Card::value).sorted().toList();
    }

    private Map<Integer, Long> getCardCounts() {
        return this.cards.stream()
                .collect(Collectors.groupingBy(Card::value, Collectors.counting()));
    }

    /**
     * Similar to a straight, but all cards in the same suit.
     */
    public boolean hasStraightFlush() {
        return hasStraight() && hasFlush();
    }

    /**
     * A, K, Q, J, 10, all the same suit.
     */
    public boolean hasRoyalFlush() {
        return hasFlush() && hasRroyalStraight();
    }

    /**
     * Returns a String representation of the cards, such as "♠12 ♠10 ♠11 ♠1 ♠13"
     */
    @Override
    public String toString() {
        List<String> asList = this.cards.stream().map(c -> c.suit() + c.value()).toList();

        return String.join(" ", asList);
    }
}
