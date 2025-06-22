package travel;

public class Session {
	public static int userId;
	public static String username;

	public static void login(int id, String user) {
		userId = id;
		username = user;
	}

	public static void logout() {
		userId = 0;
		username = null;
	}

	public static boolean isLoggedIn() {
		return username != null;
	}

	public static int getUserId() {
		return userId;
	}

	public static String getUsername() {
		return username;
	}

	public static void main(String[] args) {
		System.out.println(Session.username);
	}
}
