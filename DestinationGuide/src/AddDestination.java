
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AddDestination extends JFrame {

	JLabel nameLabel, locationLabel, descLabel, ratingLabel;
	JTextField nameField, locationField, descField, ratingField;
	JButton add;

	public AddDestination() {
		setTitle("Add destination");
		setLayout(null);
		setSize(500, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		nameLabel = new JLabel("Name");
		nameField = new JTextField(100);

		locationLabel = new JLabel("Location");
		locationField = new JTextField(100);

		descLabel = new JLabel("Description");
		descField = new JTextField(300);
		ratingLabel = new JLabel("Rating");
		ratingField = new JTextField(10);

		add = new JButton("Add");

		nameLabel.setBounds(50, 30, 120, 25);
		nameField.setBounds(180, 30, 150, 25);

		locationLabel.setBounds(50, 70, 120, 25);
		locationField.setBounds(180, 70, 150, 25);

		descLabel.setBounds(50, 110, 120, 25);
		descField.setBounds(180, 110, 150, 25);

		ratingLabel.setBounds(50, 150, 120, 25);
		ratingField.setBounds(180, 150, 150, 25);

		add.setBounds(50, 180, 120, 25);

		add(nameLabel);
		add(nameField);
		add(locationLabel);
		add(locationField);
		add(descLabel);
		add(descField);
		add(ratingLabel);
		add(ratingField);
		add(add);

		add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = nameField.getText();
				String location = locationField.getText();
				String desc = descField.getText();
				String rating = ratingField.getText();

				if (name.isEmpty() || location.isEmpty() || desc.isEmpty() || rating.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Fields cannot be empty");

				} else {

					try {
						Connection connection = Conn.getConnection();
						PreparedStatement checkSmt = connection
								.prepareStatement("SELECT id from destination where name=?");
						checkSmt.setString(1, name);
						ResultSet resultSet = checkSmt.executeQuery();
						if (resultSet.next()) {
							JOptionPane.showMessageDialog(null, "destination already exist");
							connection.close();

						}

						PreparedStatement preparedStatement = connection.prepareStatement(
								"INSERT into destination(user_id,name,location,description,avg_rating) values(?,?,?,?,?)");
						preparedStatement.setInt(1, Session.getUserId());
						preparedStatement.setString(2, name);
						preparedStatement.setString(3, location);
						preparedStatement.setString(4, desc);
						preparedStatement.setInt(5, Integer.parseInt(rating));

						int rowInserted = preparedStatement.executeUpdate();
						if (rowInserted > 0) {
							JOptionPane.showMessageDialog(null, "Record inserted");
							dispose();
							new HomePage(Session.getUserId(), Session.getUsername());
						} else {
							JOptionPane.showMessageDialog(null, "failed to insert");
						}
						connection.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "Database error : " + e1.getMessage());
					}

				}

			}
		});

		setVisible(true);

	}

	public static void main(String[] args) {
		new AddDestination();
	}

}
