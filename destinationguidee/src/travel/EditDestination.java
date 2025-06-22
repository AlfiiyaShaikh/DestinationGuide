package travel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EditDestination extends JFrame {
	JTextField nameField, locationField, descField, ratingField;
	JButton updateButton;
	int destId;

	public EditDestination(int id) {
		this.destId = id;
		setTitle("Edit Destination");
		setSize(400, 300);
		setLayout(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		JLabel nameLabel = new JLabel("Name:");
		JLabel locationLabel = new JLabel("Location:");
		JLabel descLabel = new JLabel("Description:");
		JLabel ratingLabel = new JLabel("Rating:");

		nameField = new JTextField();
		locationField = new JTextField();
		descField = new JTextField();
		ratingField = new JTextField();

		updateButton = new JButton("Update");

		nameLabel.setBounds(30, 30, 100, 25);
		nameField.setBounds(140, 30, 200, 25);
		locationLabel.setBounds(30, 70, 100, 25);
		locationField.setBounds(140, 70, 200, 25);
		descLabel.setBounds(30, 110, 100, 25);
		descField.setBounds(140, 110, 200, 25);
		ratingLabel.setBounds(30, 150, 100, 25);
		ratingField.setBounds(140, 150, 200, 25);
		updateButton.setBounds(140, 200, 100, 25);

		add(nameLabel);
		add(nameField);
		add(locationLabel);
		add(locationField);
		add(descLabel);
		add(descField);
		add(ratingLabel);
		add(ratingField);
		add(updateButton);

		// Load existing destination
		try {
			Connection con = Conn.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM destination WHERE id = ?");
			ps.setInt(1, destId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				nameField.setText(rs.getString("name"));
				locationField.setText(rs.getString("location"));
				descField.setText(rs.getString("description"));
				ratingField.setText(rs.getString("avg_rating"));
			}
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Failed to load data.");
		}

		// Update record
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Connection con = Conn.getConnection();
					PreparedStatement ps = con.prepareStatement(
							"UPDATE destination SET name = ?, location = ?, description = ?, avg_rating = ? WHERE id = ?");
					ps.setString(1, nameField.getText());
					ps.setString(2, locationField.getText());
					ps.setString(3, descField.getText());
					ps.setString(4, ratingField.getText());
					ps.setInt(5, destId);

					int rows = ps.executeUpdate();
					if (rows > 0) {
						JOptionPane.showMessageDialog(null, "Updated successfully!");
						dispose();
						new HomePage(Session.getUserId(), Session.getUsername());
					} else {
						JOptionPane.showMessageDialog(null, "Update failed.");
					}
					con.close();
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
				}
			}
		});

		setVisible(true);
	}
}
