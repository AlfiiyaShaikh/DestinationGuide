
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class HomePage extends JFrame {
	public HomePage(int id, String username) {
		setTitle("Home Page");
		setLayout(null); // you're using absolute layout
		setSize(1000, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Welcome label
		JLabel welcomeText = new JLabel("Welcome " + Session.getUsername());
		welcomeText.setBounds(50, 50, 300, 25);
		add(welcomeText);

		// Button panel (with horizontal buttons)
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 40, 10));
		buttonPanel.setBounds(50, 100, 300, 50); // x, y, width, height

		JButton addDestination = new JButton("Add Destination");
		JButton logout = new JButton("Logout");

		buttonPanel.add(addDestination);
		buttonPanel.add(logout);

		add(buttonPanel);

		JPanel cardPannel = new JPanel();
		cardPannel.setLayout(new BoxLayout(cardPannel, BoxLayout.Y_AXIS));
		cardPannel.setBounds(50, 160, 800, 500);

		try {
			Connection connection = Conn.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(
					"SELECT dest.*,user.id from destination dest join users user where dest.user_id=user.id");
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				JPanel card = new JPanel();
				card.setLayout(null);

				card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				card.setBackground(Color.LIGHT_GRAY);
				card.setSize(600, 120);

				JLabel nameLabel = new JLabel("Name: " + resultSet.getString("name"));
				JLabel locationLabel = new JLabel("Location: " + resultSet.getString("location"));
				JLabel descLabel = new JLabel("Description: " + resultSet.getString("description"));
				JLabel ratingLabel = new JLabel("Rating: " + resultSet.getString("avg_rating"));

				nameLabel.setBounds(10, 10, 200, 25);
				locationLabel.setBounds(10, 40, 200, 25);
				descLabel.setBounds(10, 70, 400, 40); // more space for description
				ratingLabel.setBounds(420, 10, 100, 25);

				card.add(nameLabel);

				card.add(locationLabel);
				card.add(descLabel);
				card.add(ratingLabel);

				// Show Edit/Delete only if added by logged-in user
				int destUserId = resultSet.getInt("user_id");
				int currentUserId = Session.getUserId();
				int destId = resultSet.getInt("id");

				if (destUserId == currentUserId) {
					JButton editButton = new JButton("Edit");
					JButton deleteButton = new JButton("Delete");

					editButton.setBounds(420, 50, 80, 25);
					deleteButton.setBounds(510, 50, 80, 25);

					// Open Edit page
					editButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							new EditDestination(destId); // pass ID to edit page
						}
					});

					// Open Delete confirm page
					deleteButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							new DeleteDestination(destId); // pass ID to delete page
						}
					});

					card.add(editButton);
					card.add(deleteButton);
				}

				cardPannel.add(card);
			}

		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		add(cardPannel);

		addDestination.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new AddDestination();

			}
		});
		logout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				Session.logout();

				new Login();

			}
		});

		setVisible(true);
	}
}
