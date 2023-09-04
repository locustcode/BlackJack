import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class BlackJackGUI extends JFrame {
    private Deck deck;
    private Hand playerHand;
    private Hand dealerHand;
    private int bankroll;
    private int bet;
    private final JFrame frame;
    private final JLabel playerHandLabel;
    private final JLabel dealerHandLabel;
    private final JLabel bankrollLabel;
    private final JLabel betMessageLabel;
    private final JTextArea playerTextArea;
    private final JTextArea dealerTextArea;
    private final JButton betButton;
    private final JButton hitButton;
    private final JButton standButton;
    private final JButton doubleDownButton;
    private final JButton splitButton;
    private final JButton playAgainButton;
    private final Color casinoGreen = new Color(0, 85, 0);




    public BlackJackGUI() throws BadLocationException, InterruptedException {
        // Initialize deck, hands, and bankroll
        deck = new Deck();
        deck.shuffle();
        playerHand = new Hand();
        dealerHand = new Hand();
        bankroll = 5000;
        bet = 0;

        // Initial card dealing)
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());

        // Create and configure the JFrame
        frame = new JFrame("Blackjack Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) ((screenSize.getWidth() - frame.getWidth()) / 2);
        int centerY = (int) ((screenSize.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(centerX, centerY);

        // Create a JTextArea for displaying messages
        playerTextArea = new JTextArea();
        dealerTextArea = new JTextArea();
        playerTextArea.setEditable(false);
        dealerTextArea.setEditable(false);
        Font largeFont = new Font("Arial", Font.BOLD, 20);
        playerTextArea.setForeground(casinoGreen);
        dealerTextArea.setForeground(casinoGreen);

        // Create and configure GUI components

        // Labels
        Font labelFont = new Font("Arial", Font.BOLD, 17);
        playerHandLabel = new JLabel("Your hand value: ");
        playerHandLabel.setFont(labelFont);
        dealerHandLabel = new JLabel("Dealer's hand: " + dealerHand.displayDealerCard());
        dealerHandLabel.setFont(labelFont);
        bankrollLabel = new JLabel("Bankroll: $" + bankroll);
        bankrollLabel.setFont(labelFont);
        betMessageLabel = new JLabel("Must place a bet before starting!");
        betMessageLabel.setFont(labelFont);

        // Buttons
        betButton = new JButton(("Bet"));
        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");
        doubleDownButton = new JButton("Double Down");
        doubleDownButton.setEnabled(false);
        splitButton = new JButton("Split");
        splitButton.setEnabled(false);
        playAgainButton = new JButton("Play Again");
        playAgainButton.setEnabled(false);

        // Text Panel and Text Areas
        JPanel textPanel = new JPanel(new GridLayout(1, 2));
        playerTextArea.setFont(largeFont);
        dealerTextArea.setFont(largeFont);
        playerTextArea.setBackground(casinoGreen);
        dealerTextArea.setBackground(casinoGreen);
        textPanel.add(playerTextArea);
        textPanel.add(dealerTextArea);

        // Add the textPanel to the frame
        frame.add(textPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        // Create and configure layout
        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Player Hand Panel
        JPanel playerHandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        playerHandPanel.add(playerHandLabel);

        // Dealer Hand Panel
        JPanel dealerHandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dealerHandPanel.add(dealerHandLabel);

        // Create Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(doubleDownButton);
        buttonPanel.add(splitButton);
        buttonPanel.add(playAgainButton);

        // Create Bet Panel
        JPanel betPanel = new JPanel();
        betPanel.setLayout(new BorderLayout());
        JPanel betLayoutPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        betLayoutPanel.add(bankrollLabel);
        JTextField textField = new JTextField(5);
        betLayoutPanel.add(textField);
        betLayoutPanel.add(betButton);
        betLayoutPanel.add(betMessageLabel);
        betPanel.add(betLayoutPanel, BorderLayout.WEST);

        // Additions to main Panel
        mainPanel.add(playerHandPanel, BorderLayout.NORTH);
        mainPanel.add(dealerHandPanel, BorderLayout.CENTER);
        mainPanel.add(betPanel, BorderLayout.SOUTH);
        mainPanel.add(buttonPanel, BorderLayout.EAST);

        // Add mainPanel to the JFrame
        frame.add(mainPanel, BorderLayout.SOUTH);

        // Update GUI components based on game state
        updateGUI();
        updateDealerGUI();

        // Display the JFrame
        frame.setVisible(true);

        //Set Default Rules
        enableGameButtons(false);
        showText(false);


        // Add action listeners to buttons

        // Hit Button
        hitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)  {
                // Handle hit action
                playerHand.addCard(deck.drawCard());
                if(playerHand.getHandValue() == 21) {
                    enableGameButtons(false);
                    updateDealerGUI();
                }else if(playerHand.getHandValue() > 21) {
                    enableGameButtons(false);
                }
                updateGUI();
            }
        });

        // Stand Button
        standButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle stand action
                enableGameButtons(false);
                updateDealerGUI();
            }
        });

        // Double Down Button
        // **IN PROGRESS**
        doubleDownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        // Split Button
        // **IN PROGRESS**
        splitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        // Play Again Button
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle play again action
                // Call the resetGame method
                resetGame();
                betMessageLabel.setVisible(true);

            }
        });

        // Bet Button
        betButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    String input = textField.getText();
                    if (isValidInteger(input) && isInRange(input, bankroll) && Integer.parseInt(input) >= 1) {
                        betButton.setEnabled(false);
                        int intValue = Integer.parseInt(input);
                        bet = intValue;
                        bankroll -= intValue;
                        updateBankroll();
                        enableGameButtons(true);
                        betMessageLabel.setVisible(false);
                        showText(true);
                    } else if (!isInRange(input, bankroll)) {
                        JOptionPane.showMessageDialog(null, "You do not have enough bankroll for this bet. Enter again.");
                    } else if (!isValidInteger(input)) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter an integer");
                    }else if(Integer.parseInt(input) < 1) {
                        JOptionPane.showMessageDialog(null, "Must bet at least $1 to continue.");
                    }
                    updateGUI();
                    updateDealerGUI();
                }
        });

    }

    // Input Validation for Bet
    private static boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Input Validation for Bet
    private static boolean isInRange(String input, int bankroll) {
        return Integer.parseInt(input) <= bankroll;
    }

    //Toggle Between TextArea visibility
    private void showText(boolean show) {
        dealerHandLabel.setVisible(show);
        playerHandLabel.setVisible(show);
        if(show) {
            playerTextArea.setForeground(Color.white);
            dealerTextArea.setForeground(Color.white);
        }else { // Blend text in with background, hiding text but keeping background
            playerTextArea.setForeground(casinoGreen);
            dealerTextArea.setForeground(casinoGreen);
        }
    }

    // Toggle Buttons
    private void enableGameButtons(boolean enable) {
        // Enable or disable game-related buttons
        hitButton.setEnabled(enable);
        standButton.setEnabled(enable);
    }

    // Make Updates to player's hand
    private void updateGUI() {
        // Update player's hand label (you can use your own formatting)
        playerHandLabel.setText("Your hand value: " + playerHand.getHandValue());

        // Create a new message with the player's hand
        String message = "Your hand:\n" + playerHand.displayHand() + "\n";
        playerTextArea.setText(message);

        // Player Hand Conditions before Dealer Draw
        if(playerHand.blackJack(playerHand) && !dealerHand.blackJack(dealerHand)) {
            enableGameButtons(false);
            playerTextArea.append("Blackjack! You win! 3 to 2 Payout.");
            enablePlayAgainButton();
            bankroll += bet + (bet * 1.5);
            updateBankroll();
        }else if(playerHand.blackJack(playerHand) && dealerHand.blackJack(dealerHand)) {
            enableGameButtons(false);
            updateDealerGUI();
            playerTextArea.append("Both have Blackjack. It is a push.");
            enablePlayAgainButton();
            bankroll += bet;
            updateBankroll();
        }else if (playerHand.getHandValue() > 21) {
            playerTextArea.append("You Bust! Dealer wins.");
            enablePlayAgainButton();
        }
    }

    // Make Updates to Dealer's Hand
    private void updateDealerGUI() {

        // Update Dealer's Face Up Card Value
        dealerHandLabel.setText("Dealer's Card Value: " + dealerHand.getDealerFirstCardValue());

        // Update Dealer's Face Up Card
        String message = "Dealer's Face Up Card:\n" + dealerHand.displayDealerCard() + "\n";
        dealerTextArea.setText(message);



        // Hit Button Disabled Triggers Dealer to Draw Cards
        if (!hitButton.isEnabled()) {
            if (dealerHand.getHandValue() >= 17) {
                int handValue = dealerHand.getHandValue();
                String handMessage = "Dealer's Hand Value: " + handValue;
                String handDisplay = "Dealer's Hand: \n" + dealerHand.displayHand() + "\n";

                // Update the GUI components on the EDT
                SwingUtilities.invokeLater(() -> {
                    dealerHandLabel.setText(handMessage);
                    dealerTextArea.setText(handDisplay);
                });
                evaluateDealerHand(dealerHand.getHandValue());
            } else {
                new Thread(() -> {


                    // Adding 1s delay allows users to process each card the dealer draws to feel the excitement of the
                    // game
                    try {
                        Thread.sleep(1000); // Add a 1s delay before showing the full hand
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while (dealerHand.getHandValue() < 17 && !playerHand.blackJack(playerHand)) {
                        dealerHand.addCard(deck.drawCard());
                        int handValue = dealerHand.getHandValue();
                        String handMessage = "Dealer's Hand Value: " + handValue;
                        String handDisplay = "Dealer's Hand: \n" + dealerHand.displayHand() + "\n";

                        // Update the GUI components on the EDT
                        SwingUtilities.invokeLater(() -> {
                            dealerHandLabel.setText(handMessage);
                            dealerTextArea.setText(handDisplay);
                        });
                        try {
                            Thread.sleep(1000); // Add a 1s delay between card additions
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    evaluateDealerHand(dealerHand.getHandValue());
                }).start();
            }
        }
    }

    // Checks all Conditions to Match Dealer's Hand and Conclude Result
    private void evaluateDealerHand(int dealerValue) {
        SwingUtilities.invokeLater(() -> {
            if (dealerValue > 21) {
                dealerTextArea.append("Dealer Busts! You Win.");
                enablePlayAgainButton();
                bankroll += (bet*2);
                updateBankroll();
            } else if (dealerValue < playerHand.getHandValue() && !playerHand.blackJack(playerHand)) {
                dealerTextArea.append("You Win!");
                enablePlayAgainButton();
                bankroll += (bet*2);
                updateBankroll();
            } else if (dealerValue > playerHand.getHandValue()) {
                dealerTextArea.append("Dealer wins.");
                enablePlayAgainButton();
            } else if (dealerValue == playerHand.getHandValue() && !playerHand.blackJack(playerHand)) {
                dealerTextArea.append("Push.");
                enablePlayAgainButton();
                enableGameButtons(false);
                bankroll += bet;
                updateBankroll();
            } else if (dealerHand.blackJack(dealerHand) && !playerHand.blackJack(playerHand)) {
                dealerTextArea.append("Blackjack. Dealer wins.");
                enablePlayAgainButton();
            }
        });
    }

    //Updates the Bankroll Label
    private void updateBankroll() {
        bankrollLabel.setText("Bankroll: $" + bankroll);
    }

    //Resets Game for Play Again Button
    private void resetGame() {

        // Clear hands and deck
        playerHand = new Hand();
        dealerHand = new Hand();
        deck = new Deck();
        playAgainButton.setEnabled(false);
        showText(false);
        betButton.setEnabled(true);
        enableGameButtons(false);

        // Reinitialize the deck, shuffle, and deal initial cards
        deck = new Deck();
        deck.shuffle();
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());
        playerHand.addCard(deck.drawCard());
        dealerHand.addCard(deck.drawCard());

        // Update the GUI's
//        updateGUI();
//        updateDealerGUI();
    }


    //Method to Enable Play Again Button Efficiently
    private void enablePlayAgainButton() {
        playAgainButton.setEnabled(true);
    }


    // Main Method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new BlackJackGUI();
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
