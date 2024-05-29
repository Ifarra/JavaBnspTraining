package uwu.testuwu;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class App 
{
    public static void main( String[] args )
    {
    	boolean QUIT = true;
    	Scanner scanner = new Scanner(System.in);
    	while(QUIT) {
	    	System.out.println("Welcome to HoloIndex");
	    	System.out.println("------------------------------------");
	        System.out.println("Pilih menu:");
	        System.out.println("[1] Cari dari atas");
	        System.out.println("[2] Cari dari bawah");
	        System.out.println("[3] Pencarian");
	        System.out.println("[4] Tambah data");
	        System.out.println("[5] Update data");
	        System.out.println("[6] Hapus data");
	        System.out.println("[0] QUIT");
	        int pilihMenu = scanner.nextInt();
	        System.out.println("------------------------------------");
	        switch (pilihMenu) {
			case 1:
				System.out.println("Filter dari table apa?");
				System.out.println("[1] id");
				System.out.println("[2] name");
				System.out.println("[3] gen");
				System.out.println("[4] debut");
				int pilihFilter1 = scanner.nextInt();
				switch (pilihFilter1) {
				case 1:
					selectAsc("id");
					break;
				case 2:
					selectAsc("name");
					break;
				case 3:
					selectAsc("gen");
					break;
				case 4:
					selectAsc("debut_date");
					break;
				default:
					break;
				}
				break;
			case 2:
				System.out.println("Filter dari table apa?");
				System.out.println("[1] id");
				System.out.println("[2] name");
				System.out.println("[3] gen");
				System.out.println("[4] debut");
				int pilihFilter2 = scanner.nextInt();
				switch (pilihFilter2) {
				case 1:
					selectDesc("id");
					break;
				case 2:
					selectDesc("name");
					break;
				case 3:
					selectDesc("gen");
					break;
				case 4:
					selectDesc("debut_date");
					break;
				default:
					break;
				}
				break;
			case 3:
				System.out.println("Cari dari table apa?");
				System.out.println("[1] id");
				System.out.println("[2] name");
				System.out.println("[3] gen");
				System.out.println("[4] debut");
				int pilihFilter3 = scanner.nextInt();
				switch (pilihFilter3) {
				case 1:
					String searchScan1 = scanner.next();
					search(searchScan1, "id");
					break;
				case 2:
					String searchScan2 = scanner.next();
					search(searchScan2, "name");
					break;
				case 3:
					String searchScan3 = scanner.next();
					search(searchScan3, "gen");
					break;
				case 4:
					String searchScan4 = scanner.next();
					search(searchScan4, "debut_date");
					break;
				default:
					break;
				}
				break;
			case 4:
				System.out.println("Masukan nama talent: (contoh = Moona Hoshinova)");
				String insert1 = scanner.next();
				System.out.println("Masukan gen talent: (contoh = Hololive Indonesia 1st Generation)");
				scanner.nextLine();
				String insert2 = scanner.nextLine();
				System.out.println("Masukan debut date talent: (contoh = 2020-11-17)");
				String insert3 = scanner.nextLine();
				try {
					insert(insert1, insert2, insert3);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 5:
				System.out.println("Masukan id talent: (contoh = 1)");
				int update1 = scanner.nextInt();
				System.out.println("Masukan nama talent: (contoh = Moona Hoshinova)");
				scanner.nextLine();
				String update2 = scanner.nextLine();
				System.out.println("Masukan gen talent: (contoh = Hololive Indonesia 1st Generation)");
				String update3 = scanner.nextLine();
				try {
					update(update1, update2, update3);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 6:
				System.out.println("Masukan id talent: (contoh = 1)");
				int delete1 = scanner.nextInt();
				try {
					delete(delete1);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 0:
				System.out.println("-------------END APP----------------");
				System.out.println("------------------------------------");
				QUIT = false;
				break;
			default:
				break;
			}
    	}
    	scanner.close();
    }
    
    public static void selectDesc(String column_name) {
        try (Connection connection = App.getConnection()) {
            if (connection != null) {
                // Perform database operations here
                String query = "SELECT * FROM user ORDER BY "+ column_name +" DESC";
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = statement.executeQuery(query);
                App.printResultSetAsTable(resultSet, 150);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
	public static void selectAsc(String column_name) {
        try (Connection connection = App.getConnection()) {
            if (connection != null) {
                // Perform database operations here
                String query = "SELECT * FROM user ORDER BY "+ column_name;
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = statement.executeQuery(query);
                App.printResultSetAsTable(resultSet, 150);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void search(String searchTerm, String column_name) {
        try (Connection connection = App.getConnection()){
        		String searchTerms = "'%" +searchTerm+ "%'";
        		String query = "SELECT * FROM user WHERE "+column_name+" LIKE "+searchTerms;
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	        	ResultSet rs = statement.executeQuery(query);
	        	App.printResultSetAsTable(rs, 150);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void insert(String name, String gen, String debutDate) throws SQLException {
        String sql = "INSERT INTO user (name, gen, debut_date) VALUES (?, ?, ?)";
        try (Connection connection = App.getConnection()) {
        	PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, gen);
            stmt.setString(3, debutDate);
            stmt.executeUpdate();
            System.out.println("Insert operation successful.");
        }
    }
    
    public static void delete(int id) throws SQLException {
        String sql = "DELETE FROM user WHERE id = ?";
        try (Connection conn = App.getConnection()) {
        	PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Delete operation successful.");
            } else {
                System.out.println("No row found with the given id.");
            }
        }
    }
    
    public static void update(int id, String name, String gen) throws SQLException {
        String sql = "UPDATE user SET name = ?, gen = ? WHERE id = ?";
        try (Connection conn = App.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, gen);
            stmt.setInt(3, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Update operation successful.");
            } else {
                System.out.println("No row found with the given id.");
            }
        }
    }
    
    public static Connection getConnection() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Specify the connection details
            String url = "jdbc:mysql://localhost:3306/test";
            String username = "root";
            String password = "";

            // Establish the connection
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void printResultSetAsTable(ResultSet resultSet, int tableWidth) {
        try {
            // Make the result set scrollable
            resultSet.beforeFirst();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            int[] columnWidths = new int[columnCount];

            // Determine the maximum width for each column
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    int columnWidth = metaData.getColumnDisplaySize(i);
                    if (resultSet.getObject(i) != null) {
                        int objectWidth = resultSet.getObject(i).toString().length();
                        columnWidths[i - 1] = Math.min(Math.max(columnWidth, objectWidth), tableWidth / columnCount);
                    }
                }
            }

            // Reset the result set cursor to the beginning
            resultSet.beforeFirst();

            // Print the separator line
            for (int i = 1; i <= columnCount; i++) {
                System.out.print("+-" + "-".repeat(columnWidths[i - 1]) + "-");
            }
            System.out.println("+");

            // Print the column headers
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("| %-" + (columnWidths[i - 1] > 0 ? columnWidths[i - 1] : 1) + "s ", metaData.getColumnName(i));
            }
            System.out.println("|");

            // Print the separator line
            for (int i = 1; i <= columnCount; i++) {
                System.out.print("+-" + "-".repeat(columnWidths[i - 1]) + "-");
            }
            System.out.println("+");

            // Print the row data
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.printf("| %-" + (columnWidths[i - 1] > 0 ? columnWidths[i - 1] : 1) + "s ", resultSet.getObject(i));
                }
                System.out.println("|");
            }

            // Print the separator line
            for (int i = 1; i <= columnCount; i++) {
                System.out.print("+-" + "-".repeat(columnWidths[i - 1]) + "-");
            }
            System.out.println("+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
