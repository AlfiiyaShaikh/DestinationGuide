package travel;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class DeleteDestination extends JFrame {
	public DeleteDestination(int id) {
		int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this destination?",
				"Confirm Delete", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			try {
				Connection con = Conn.getConnection();
				PreparedStatement ps = con.prepareStatement("DELETE FROM destination WHERE id = ?");
				ps.setInt(1, id);
				int rows = ps.executeUpdate();
				if (rows > 0) {
					JOptionPane.showMessageDialog(null, "Deleted successfully.");
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Delete failed.");
				}
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
			}
		}
		// Refresh HomePage after delete
		new HomePage(Session.getUserId(), Session.getUsername());
	}
}
