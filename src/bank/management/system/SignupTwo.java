package bank.management.system;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class SignupTwo extends JFrame implements ActionListener{
    
    JTextField pan, aadhar;
    JButton next;
    JRadioButton syes, sno, eyes, eno;
    JComboBox Religion, Category, Income, education, Occupation;
    String formno;
    
    SignupTwo(String formno) {
        
        this.formno = formno;
        setLayout(null);
        
        setTitle("NEW ACCOUNT APPLICATION FORM - PAGE 2");
        
        JLabel additionalDetails = new JLabel("Page 2: Additional Details");
        additionalDetails.setFont(new Font("Railway", Font.BOLD, 22));
        additionalDetails.setBounds(290, 80, 400, 30);
        add(additionalDetails);
        
        //Religion
        JLabel religion = new JLabel("Religion:");
        religion.setFont(new Font("Railway", Font.BOLD, 22));
        religion.setBounds(100, 140, 100, 30);
        add(religion);
        
        String valReligion[] = {"Hindu", "Muslim", "Sikh", "Christian", "Other"};
        Religion = new JComboBox(valReligion);
        Religion.setBounds(300, 140, 400, 30);
        Religion.setBackground(Color.WHITE);
        add(Religion);        
        
        //Category
        JLabel category = new JLabel("Category:");
        category.setFont(new Font("Railway", Font.BOLD, 22));
        category.setBounds(100, 190, 200, 30);
        add(category);
        
        String valCategory[] = {"General", "OBC", "SC", "ST", "Other"};
        Category = new JComboBox(valCategory);
        Category.setBounds(300, 190, 400, 30);
        Category.setBackground(Color.WHITE);
        add(Category);        
        
        //Income
        JLabel income = new JLabel("Income:");
        income.setFont(new Font("Railway", Font.BOLD, 22));
        income.setBounds(100, 240, 200, 30);
        add(income);
        
        String incomecategory[] = {"Null", "< 1,50,000", "< 2,50,000", "< 5,00,000", "Upto 10,00,000"};
        Income = new JComboBox(incomecategory);
        Income.setBounds(300, 240, 400, 30);
        Income.setBackground(Color.WHITE);
        add(Income);
        
        //Educational Qualification
        JLabel educational = new JLabel("Educational:");
        educational.setFont(new Font("Railway", Font.BOLD, 22));
        educational.setBounds(100, 290, 200, 30);
        add(educational);
        
        JLabel qualification = new JLabel("Qualification:");
        qualification.setFont(new Font("Railway", Font.BOLD, 22));
        qualification.setBounds(100, 315, 200, 30);
        add(qualification);
        
        String educationValues[] = {"Non-Graduation", "Graduate", "Post-Graduation", "Doctrate", "Others"};
        education = new JComboBox(educationValues);
        education.setBounds(300, 315, 400, 30);
        education.setBackground(Color.WHITE);
        add(education);
        
        //Occupation
        JLabel occupation = new JLabel("Occupation:");
        occupation.setFont(new Font("Railway", Font.BOLD, 22));
        occupation.setBounds(100, 390, 200, 30);
        add(occupation);
        
        String occupationValues[] = {"Salaried", "Self-Employed", "Bussiness", "Student", "Retired", "Others"};
        Occupation = new JComboBox(occupationValues);
        Occupation.setBounds(300, 390, 400, 30);
        Occupation.setBackground(Color.WHITE);
        add(Occupation);
        
        //PAN Number
        JLabel Pan = new JLabel("PAN Number:");
        Pan.setFont(new Font("Railway", Font.BOLD, 22));
        Pan.setBounds(100, 440, 200, 30);
        add(Pan);
        
        pan = new JTextField();
        pan.setFont(new Font("Railway", Font.BOLD, 14));
        pan.setBounds(300, 440, 400, 30);
        add(pan);
        
        //Aadhar Number
        JLabel aadharNumber = new JLabel("Aadhar Number:");
        aadharNumber.setFont(new Font("Railway", Font.BOLD, 22));
        aadharNumber.setBounds(100, 490, 200, 30);
        add(aadharNumber);
        
        aadhar = new JTextField();
        aadhar.setFont(new Font("Railway", Font.BOLD, 14));
        aadhar.setBounds(300, 490, 400, 30);
        add(aadhar);
        
        //Senior Citizen
        JLabel seniorcitizen = new JLabel("Senior Citizen:");
        seniorcitizen.setFont(new Font("Railway", Font.BOLD, 22));
        seniorcitizen.setBounds(100, 540, 200, 30);
        add(seniorcitizen);
        
        syes = new JRadioButton("Yes");
        syes.setBounds(300, 540, 100, 30);
        syes.setBackground(Color.WHITE);
        add(syes);
        
        sno = new JRadioButton("No");
        sno.setBounds(450, 540, 100, 30);
        sno.setBackground(Color.WHITE);
        add(sno);
        
        ButtonGroup senior = new ButtonGroup();
        senior.add(syes);
        senior.add(sno);
        
        //Existing Account
        JLabel existingaccount = new JLabel("Existing Account:");
        existingaccount.setFont(new Font("Railway", Font.BOLD, 22));
        existingaccount.setBounds(100, 590, 200, 30);
        add(existingaccount);
        
        eyes = new JRadioButton("Yes");
        eyes.setBounds(300, 590, 100, 30);
        eyes.setBackground(Color.WHITE);
        add(eyes);
        
        eno = new JRadioButton("No");
        eno.setBounds(450, 590, 100, 30);
        eno.setBackground(Color.WHITE);
        add(eno);
        
        ButtonGroup exisacc = new ButtonGroup();
        exisacc.add(syes);
        exisacc.add(sno);
        
        next = new JButton("Next");
        next.setBackground(Color.BLACK);
        next.setForeground(Color.WHITE);
        next.setFont(new Font("Railway", Font.BOLD, 14));
        next.setBounds(620, 660, 80, 30);
        next.addActionListener(this);
        add(next);
        
        getContentPane().setBackground(Color.WHITE);
        
        setSize(850, 800);
        setLocation(350, 10);
        setVisible(true);
        
    }
    
    public void actionPerformed(ActionEvent ae){
        String religion = (String) Religion.getSelectedItem();
        String category = (String) Category.getSelectedItem();
        String income = (String) Income.getSelectedItem();
        String educational = (String) education.getSelectedItem();
        String occupation = (String) Occupation.getSelectedItem();
        String seniorcitizen = null;
        if (syes.isSelected()) {
            seniorcitizen = "Yes";
        } else if (sno.isSelected()) {
            seniorcitizen = "No";
        }
        
        String existingaccount = null;
        if (eyes.isSelected()) {
            existingaccount = "Yes";
        } else if (eno.isSelected()) {
            existingaccount = "no";
        }
        
        String Pan = pan.getText();
        String aadharNumber = aadhar.getText();        
        
        try {
            Conn c = new Conn();
            String query = "insert into signuptwo values('"+formno+"','"+religion+"','"+category+"','"+income+"','"+educational+"','"+occupation+"','"+Pan+"','"+aadharNumber+"','"+seniorcitizen+"','"+existingaccount+"')";
            c.s.executeUpdate(query);
            
            //Signup3
            setVisible(false);
            new Signup3(formno).setVisible(true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    
    
    public static void main (String args[]) {
        new SignupTwo("");
    }
}
