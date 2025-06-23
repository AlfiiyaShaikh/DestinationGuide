
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Registration extends JFrame {

	// Components
	JLabel usernameLabel, passwordLabel, confirmPasswordLabel;
	JTextField usernameField;
	JPasswordField passwordField, confirmPasswordField;
	JButton submitButton, backButton;

	public Registration() {
		setTitle("User Registration");
		setLayout(null);
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // center on screen

		// Labels and Fields
		usernameLabel = new JLabel("Username:");
		usernameField = new JTextField(20);

		passwordLabel = new JLabel("Password:");
		passwordField = new JPasswordField(20);

		confirmPasswordLabel = new JLabel("Confirm Password:");
		confirmPasswordField = new JPasswordField(20);

		submitButton = new JButton("Register");
		backButton = new JButton("Back to Login");

		// Positioning
		usernameLabel.setBounds(50, 30, 120, 25);
		usernameField.setBounds(180, 30, 150, 25);

		passwordLabel.setBounds(50, 70, 120, 25);
		passwordField.setBounds(180, 70, 150, 25);

		confirmPasswordLabel.setBounds(50, 110, 120, 25);
		confirmPasswordField.setBounds(180, 110, 150, 25);

		submitButton.setBounds(50, 160, 120, 30);
		backButton.setBounds(200, 160, 130, 30);

		// Add to Frame
		add(usernameLabel);
		add(usernameField);
		add(passwordLabel);
		add(passwordField);
		add(confirmPasswordLabel);
		add(confirmPasswordField);
		add(submitButton);
		add(backButton);

		// Button Actions
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText().trim();
				String password = new String(passwordField.getPassword());
				String confirmPassword = new String(confirmPasswordField.getPassword());

				if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please fill all fields.");
				} else if (!password.equals(confirmPassword)) {
					JOptionPane.showMessageDialog(null, "Passwords do not match.");
				} else {
					try {
						Connection conn = Conn.getConnection();

						// Check if username already exists
						PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
						checkStmt.setString(1, username);
						ResultSet rs = checkStmt.executeQuery();

						if (rs.next()) {
							JOptionPane.showMessageDialog(null, "Username already exists. Please choose another.");
							conn.close();
							return;
						}

						// Insert user and get generated ID
						PreparedStatement insertStmt = conn.prepareStatement(
								"INSERT INTO users (username, password) VALUES (?, ?)",
								Statement.RETURN_GENERATED_KEYS);
						insertStmt.setString(1, username);
						insertStmt.setString(2, password);

						int rowsInserted = insertStmt.executeUpdate();

						if (rowsInserted > 0) {

							JOptionPane.showMessageDialog(null, "Registration successful!");
							dispose();
							new Login();

						} else {
							JOptionPane.showMessageDialog(null, "Failed to register.");
						}

						conn.close();

					} catch (Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
					}
				}
			}
		});

		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Login();
			}
		});

		setVisible(true);
	}

	public static void main(String[] args) {
		new Registration();
	}
}
