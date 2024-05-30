# Java Bnsp Snippet
By Ifarra

### Function Connection to database
```java
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
```

### Function Print Table
```java
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
```

### Select dari atas berdasarkan column
```java
	public static String selectAsc(String column_name) {
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
            return "Failed";
        }
        return "Sucess";
    }
```

### Select dari bawah berdasarkan column
```java
    public static String selectDesc(String column_name) {
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
            return "Failed";
        }
        return "Sucess";
    }
```

### Search berdasarkan karakter input
```java
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
```

### Insert to database
```java
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
```

### Update database
```java
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
```

### Delete database
```java
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
```
