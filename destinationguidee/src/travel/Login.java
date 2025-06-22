package travel;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Login extends JFrame {

	JLabel usernameLabel, passwordLabel;
	JTextField usernameField, passwordField;
	Button loginButton;

	public Login() {
		setTitle("User Login");
		setLayout(null);
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // center on screen

		// Labels and Fields
		usernameLabel = new JLabel("Username");
		usernameField = new JTextField(20);

		passwordLabel = new JLabel("Password");
		passwordField = new JTextField(20);

		loginButton = new Button("Login");

		// Positioning
		usernameLabel.setBounds(50, 50, 100, 25);
		usernameField.setBounds(160, 50, 150, 25);

		passwordLabel.setBounds(50, 100, 100, 25);
		passwordField.setBounds(160, 100, 150, 25);

		loginButton.setBounds(120, 160, 100, 30);

		// Adding to frame
		add(usernameLabel);
		add(usernameField);
		add(passwordLabel);
		add(passwordField);
		add(loginButton);

		setVisible(true);

		// Login Button Action
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText().trim();
				String password = passwordField.getText();
				if (username.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please fill all fields");

				} else {
					Connection connection = Conn.getConnection();

					PreparedStatement preparedStatement;
					try {
						preparedStatement = connection.prepareStatement("SELECT * from users where username=?");
						preparedStatement.setString(1, username);
						ResultSet resultSet = preparedStatement.executeQuery();

						if (resultSet.next()) {
							int id = resultSet.getInt("id");
							Session.login(id, username);

							String dbPass = resultSet.getString("password");

							if (dbPass.equals(password)) {
								Session.login(id, username);
								dispose();
								new HomePage(id, username);
							} else {
								JOptionPane.showMessageDialog(null, "Password does not match☹️!! Retry !!");
							}

						} else {
							JOptionPane.showMessageDialog(null, "User not Found");
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
		});
	}

	public static void main(String[] args) {
		new Login();
	}
}
