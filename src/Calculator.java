import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator {
    public enum Operations {
        PLUS("+") {
            public Double operate(Double a, Double b) {
                return a + b;
            }
        } ,
        MINUS("-") {
            public Double operate(Double a, Double b) {
                return a - b;
            }
        } ,
        MULTIPLY("x") {
            public Double operate(Double a, Double b) {
                return a * b;
            }
        } ,
        DIVIDE("/") {
            public Double operate(Double a, Double b) throws ArithmeticException {
                return a / b;
            }
        } ,
        EQUALS("=") {
            public Double operate(Double a, Double b) {
                return 0d;
            }
        } ,
        InvalidOperation("Invalid Operation") {
            public Double operate(Double a, Double b) {return null;}
        };
        public abstract Double operate(Double a, Double b);
        Double value;
        String s;
        Operations(String a) {
            s = a;
            value = 0d;
        }
        public static Operations getOperation(String a) {
            for(Operations o: Operations.values()) {
                if (o.s.equals(a))
                    return o;
            }
            return Operations.InvalidOperation;
        }

    }
    private static JFrame mainFrame;
    private static JLabel expression = new JLabel("0");
    private static JPanel buttonsPanel = new JPanel();
    private static JPanel numbersPanel = new JPanel(new GridLayout(6,4,2,2));
    private static JButton[][] buttonArray = new JButton[][] {
            {new JButton("%"), new JButton("sqrt"), new JButton("sqr"), new JButton("1/x")},
            {new JButton("CE"), new JButton("C"), new JButton("<-"), new JButton("/")},
            {new JButton("1"), new JButton("2"), new JButton("3"), new JButton("x")},
            {new JButton("4"), new JButton("5"), new JButton("6"), new JButton("-")},
            {new JButton("7"), new JButton("8"), new JButton("9"), new JButton("+")},
            {new JButton("+/-"), new JButton("0"), new JButton("."), new JButton("=")}
    };
    private GridBagConstraints gbc;
    private static Color onHoverColor = new Color(89, 89, 89, 200);
    private static Color mainBodyColor = new Color(91, 91, 91);
    private static Color buttonColor = new Color(46, 46, 46, 255);//(19, 19, 19, 241);
    private static int width = 400;
    private static int height = 550;

    public Calculator() {
        addJFrame();
        addMenu();
        addTextField();
        addButtons();
        mainFrame.setVisible(true);
    }

    private void addJFrame() {
        mainFrame = new JFrame("Calculator");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(width, height);
        mainFrame.getContentPane().setBackground(mainBodyColor);
        mainFrame.setLayout(new GridBagLayout());
        //mainFrame.setUndecorated(true);
        //mainFrame.setOpacity(0.99f);
        gbc = new GridBagConstraints();
        //mainFrame.setResizable(true);
    }

    private void addMenu() {
        JPanel menu = new JPanel();
        JButton menuButton = new JButton("menu");
        JLabel calculatorType = new JLabel("Calculator");
        JButton history = new JButton("His");
        history.setFocusable(false);
        menuButton.setFocusable(false);
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setOpaque(true);
        //menu.setBounds(3,3,30,30);
        left.add(menuButton);
        left.add(calculatorType);
        menu.add(left);
        menu.add(history);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainFrame.add(menu, gbc);
    }

    private void addTextField() {
        //Rectangle textFieldRect = new Rectangle(10, 10, mainFrame.getWidth() - 60, mainFrame.getHeight()/10);
        //expression.setBounds(textFieldRect);
        expression.setOpaque(false);
        expression.setBorder(BorderFactory.createEmptyBorder());
        expression.setHorizontalAlignment(SwingConstants.RIGHT);
        expression.setForeground(Color.WHITE);
        expression.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
        mainFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                showValue(e.getKeyChar() + "");
            }
        });
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainFrame.add(expression, gbc);
    }

    private void addButtons() {
        Rectangle buttonsPanelBounds = new Rectangle(0, expression.getHeight() + 20, mainFrame.getWidth(),
                mainFrame.getHeight() - expression.getHeight() - 20);
        Rectangle numberPanelBounds = new Rectangle(buttonsPanel.getX() + 5, buttonsPanel.getY() + 5,
                buttonsPanel.getWidth() - 10, buttonsPanel.getHeight() - 10);
        buttonsPanel.setOpaque(false);
        numbersPanel.setOpaque(false);
        buttonsPanel.setBounds(buttonsPanelBounds);
        numbersPanel.setBounds(numberPanelBounds);

        for (JButton[] jButtons : buttonArray) {
            for (JButton jButton : jButtons) {
                final String s = jButton.getText();
                jButton.setBackground(buttonColor);
                jButton.setForeground(Color.lightGray);
                jButton.setFocusable(false);
                jButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);
                        ((JButton) e.getSource()).setBackground(buttonColor);
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        showValue(s);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        ((JButton) e.getSource()).setBackground(onHoverColor);
                    }
                });
                numbersPanel.add(jButton);
            }
        }
        buttonsPanel.add(numbersPanel);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainFrame.add(buttonsPanel, gbc);
    }
    private void showValue(String s) {
        if (Operations.getOperation(s.substring(s.length() - 1)) != Operations.InvalidOperation) {
            expression.setText(s.substring(0, s.length() - 1) + s);
            return;
        } else if (!"1234567890".contains(s)) return;
        if (expression.getText().equals("0")) {
            expression.setText(s);
            return;
        }
        expression.setText(expression.getText() + s);
    }

    public static void main(String[] args) {
        Calculator calc = new Calculator();
    }
}
