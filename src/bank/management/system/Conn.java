/*
Steps FOr JDBC Connectivity
1. Register the driver
2. Create Connection
3. Create Statement
4. Execute query
5. Close Coonection
*/
package bank.management.system;

import java.sql.*;
public class Conn {
    
    Connection c;
    Statement s;
    public Conn() {
        try {
            c = DriverManager.getConnection("jdbc:mysql:///bankmanagementsystem", "root", "@am12082013");
            s = c.createStatement();
        
        } catch (Exception e) {
            System.out.println(e);
                
        }
    }
}
