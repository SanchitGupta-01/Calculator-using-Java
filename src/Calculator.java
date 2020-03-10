import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Calculator {
    private static JFrame mainFrame;
    private static JLabel expression = new JLabel("0");
    private static JLabel exp_label = new JLabel("");
    private static JPanel buttonsPanel = new JPanel(new GridLayout(6, 4, 2, 2));
    private static JButton[][] buttonArray = new JButton[][]{
            {new JButton("%"), new JButton("CE"), new JButton("C"), new JButton("<-")},
            {new JButton("1/x"), new JButton("sqr"), new JButton("sqrt"), new JButton("/")},
            {new JButton("1"), new JButton("2"), new JButton("3"), new JButton("x")},
            {new JButton("4"), new JButton("5"), new JButton("6"), new JButton("-")},
            {new JButton("7"), new JButton("8"), new JButton("9"), new JButton("+")},
            {new JButton("+/-"), new JButton("0"), new JButton("."), new JButton("=")}
    };
    private static Color onHoverColor = new Color(89, 89, 89, 200);
    private static Color mainBodyColor = new Color(91, 91, 91);
    private static Color buttonColor = new Color(46, 46, 46, 255);//(19, 19, 19, 241);
    private static int width = 400;
    private static int height = 550;
    private static GridBagConstraints gbc;
    private static String lastOp = null;
    private static Double lastNo = 0d;
    private static Double result = 0d;

    public Calculator() {
        addJFrame();
        addMenu();
        addTextField();
        addButtons();
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        Calculator calc = new Calculator();
    }

    private void addJFrame() {
        mainFrame = new JFrame("Calculator");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setMinimumSize(new Dimension(300, 300));
        mainFrame.setSize(width, height);
        mainFrame.getContentPane().setBackground(mainBodyColor);
        mainFrame.setLayout(new GridBagLayout());
        //mainFrame.setUndecorated(true);
        //mainFrame.setOpacity(0.99f);
        gbc = new GridBagConstraints();
        //mainFrame.setResizable(true);
    }

    private void addMenu() {
        JPanel menu = new JPanel(new BorderLayout(10, 5));
        menu.setSize(mainFrame.getWidth(), 800);
        menu.setOpaque(false);


        JButton menuButton = new JButton("menu");
        JLabel cType = new JLabel("Calculator");
        JButton history = new JButton("H");
        cType.setForeground(Color.WHITE);
        history.setBackground(buttonColor);
        history.setForeground(Color.lightGray);
        history.setFocusable(false);
        history.setSize(80 , 80);
        menuButton.setBackground(buttonColor);
        menuButton.setForeground(Color.lightGray);
        menuButton.setFocusable(false);

        menu.add(menuButton, BorderLayout.WEST);
        menu.add(cType, BorderLayout.CENTER);
        menu.add(history, BorderLayout.EAST);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        var a = (GridBagConstraints)gbc.clone();
        a.anchor = GridBagConstraints.NORTH;
        mainFrame.add(menu, a);
    }

    private void addTextField() {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setOpaque(false);
        exp_label.setForeground(new Color(155, 155, 155));
        exp_label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        exp_label.setHorizontalAlignment(SwingConstants.RIGHT);
        exp_label.setVerticalAlignment(SwingConstants.BOTTOM);
        expression.setOpaque(false);
        expression.setBorder(BorderFactory.createEmptyBorder());
        expression.setHorizontalAlignment(SwingConstants.RIGHT);
        expression.setVerticalAlignment(SwingConstants.TOP);
        expression.setForeground(Color.WHITE);
        expression.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
        mainFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                setValue(e.getKeyChar() + "", lastOp);
            }
        });
        p.add(exp_label);
        p.add(expression);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1;
        mainFrame.add(p, gbc);
    }

    private void addButtons() {
        buttonsPanel.setOpaque(false);
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
                        setValue(s, lastOp);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        ((JButton) e.getSource()).setBackground(onHoverColor);
                    }
                });
                buttonsPanel.add(jButton);
            }
        }
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 5;
        mainFrame.add(buttonsPanel, gbc);
    }

    private void operate(String s) {
        double a = 0d;
        try {
            a = Double.parseDouble(s);
        } catch(Exception e) {
            System.out.println("weee");
        }
        result = Operations.getOperation(lastOp).operate(result, a);
        lastOp = null;
        lastNo = a;
    }

    private void setValue(String s, String op) {
        if (expression.getText().equals("0")) {
            expression.setText(s);
            result = Double.parseDouble(s);
            lastNo = result;
            return;
        }

        if (expression.getText().equals(result.toString())) {
            if ("1234567890".contains(s)) {
                expression.setText(s);
            } else {
                String str = exp_label.getText();
                exp_label.setText(str.substring(0, str.length()-1) + s);
                lastOp = s;
            }
            return;
        }

        if (!"1234567890".contains(s)) { // when operation and no last op
            if (lastOp != null) {
                try {
                    operate(expression.getText());
                } catch(Exception e) {
                    System.out.println("Error - " + e.getMessage());
                }
            }
            String[] sar = new String[] {"sqr", "sqrt", "<-", "C", ".", "1/x"};
            if (Arrays.asList(sar).contains(s)) {
                System.out.println("ar");
            }
            lastOp = s;
            exp_label.setText(exp_label.getText() + lastNo + s);
            expression.setText(result.toString());
            return;
        }

        expression.setText(expression.getText() + s);
        lastNo = Double.parseDouble(lastNo.intValue() + s);
    }

    public enum Operations {
        PLUS("+") {
            public Double operate(Double a, Double b) {
                return a + b;
            }
        },
        MINUS("-") {
            public Double operate(Double a, Double b) {
                return a - b;
            }
        },
        MULTIPLY("x") {
            public Double operate(Double a, Double b) {
                return a * b;
            }
        },
        DIVIDE("/") {
            public Double operate(Double a, Double b) throws ArithmeticException {
                return a / b;
            }
        },
        EQUALS("=") {
            public Double operate(Double a, Double b) {
                return a;
            }
        },
        C("C") {
            public Double operate(Double a, Double b) {
                expression.setText("0");
                exp_label.setText("");
                lastNo = 0d;
                return 0d;
            }
        },
        BACKSPACE("<-") {
            public Double operate(Double a, Double b) {
                String s = exp_label.getText();
                if (Operations.getOperation(String.valueOf(s.charAt(s.length()-1))) != Operations.InvalidOperation) {
                    expression.setText(lastNo.toString());
                    exp_label.setText(s.substring(0, s.length()-1));
                } else {
                    lastNo = Double.parseDouble(lastNo.toString().substring(0, lastNo.toString().length() - 1));
                }
                return result;
            }
        },
        RECIPROCAL("1/x") {
            public Double operate(Double a, Double b) {
                return (double)1/result;
            }
        },
        SQR("sqr") {
            public Double operate(Double a, Double b) {
                EQUALS.operate(a, b);
                return result;
            }
        },
        SQRT("sqrt") {
            public Double operate(Double a, Double b) {
                return Math.sqrt(a);
            }
        },
        InvalidOperation("Invalid Operation") {
            public Double operate(Double a, Double b) {
                return null;
            }
        };

        Double value;
        String s;

        Operations(String a) {
            s = a;
            value = 0d;
        }

        public static Operations getOperation(String a) {
            for (Operations o : Operations.values()) {
                if (o.s.equals(a))
                    return o;
            }
            return Operations.InvalidOperation;
        }

        public abstract Double operate(Double a, Double b);

    }
}
