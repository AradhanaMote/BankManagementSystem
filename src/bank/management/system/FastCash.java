package bank.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Date;

public class FastCash extends JFrame implements ActionListener{
    
    JButton hund, fivehund, thous, twothous, fivethous, tenthous, back;
    String pin;
    
    FastCash (String pin) {
        this.pin = pin;
        setLayout(null);
        
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/atm.jpg"));
        Image i2 = i1.getImage().getScaledInstance(900, 900, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);
        image.setBounds(0, 0, 900, 900);
        add(image);
        
        JLabel text = new JLabel("Select withdrawal amount");
        text.setBounds(210, 300, 700, 35);
        text.setForeground(Color.WHITE);
        text.setFont(new Font("System", Font.BOLD, 16));
        image.add(text);
        
        hund = new JButton("Rs 100");
        hund.setBounds(170, 415, 150, 30);
        hund.addActionListener(this);
        image.add(hund);
        
        fivehund = new JButton("Rs 500");
        fivehund.setBounds(355, 415, 150, 30);
        fivehund.addActionListener(this);
        image.add(fivehund);
        
        thous = new JButton("Rs 1000");
        thous.setBounds(170, 450, 150, 30);
        thous.addActionListener(this);
        image.add(thous);
        
        twothous = new JButton("Rs 2000");
        twothous.setBounds(355, 450, 150, 30);
        twothous.addActionListener(this);
        image.add(twothous);
        
        fivethous = new JButton("Rs 5000");
        fivethous.setBounds(170, 485, 150, 30);
        fivethous.addActionListener(this);
        image.add(fivethous);
        
        tenthous = new JButton("Rs 10000");
        tenthous.setBounds(355, 485, 150, 30);
        tenthous.addActionListener(this);
        image.add(tenthous);
        
        back = new JButton("BACK");
        back.setBounds(355, 520, 150, 30);
        back.addActionListener(this);
        image.add(back);
        
        setSize(900, 900);
        setLocation(300, 10);
        //setUndecorated(true);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == back) {
            setVisible(false);
            new Transactions(pin).setVisible(true);
        }else {
            String amount = ((JButton)ae.getSource()).getText().substring(3);
            Conn c = new Conn();
            try {
                ResultSet rs = c.s.executeQuery("select * from bank where pin = '"+pin+"'");
                int balance = 0;
                while(rs.next()) {
                    if (rs.getString("type").equals("Deposit")) {
                        balance += Integer.parseInt(rs.getString("amount"));
                    }else {
                        balance -= Integer.parseInt(rs.getString("amount"));
                    }
                }
                
                if (ae.getSource() != back && balance < Integer.parseInt(amount)) {
                    JOptionPane.showMessageDialog(null, "Insufficient Balance");
                    return;
                }
                
                Date date = new Date();
                String query = "insert into bank values('"+pin+"', '"+date+"', 'Withdrawal','"+amount+"')";
                c.s.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Rs " + amount + " Debited Sucessfull");
                
                setVisible(false);
                new Transactions(pin).setVisible(true);
            }catch (Exception e) {
                System.out.println(e);
            }
        }
    } 
    
    public static void main (String args[]) {
        new FastCash("");
    }
}
