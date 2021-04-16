package pl.coderslab.entity;

import pl.coderslab.utils.BCrypt;
import pl.coderslab.utils.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UserDao {

    private static final String CREATE_USER_QUERY = "INSERT INTO users(username, email, password) VALUES (?, ?, ?); ";
    private static final String READ_USER_QUERY = "SELECT id,email,username,password FROM users WHERE id=(?);";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email=?,username= ?,password=? WHERE id=?;";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id=?;";
    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users;";
    private static final String SELECT_USER_ID = "SELECT id FROM users ORDER BY id ASC;";

    public String hashPassword(String password) {

        return BCrypt.hashpw(password, BCrypt.gensalt());

    }

    //------------------------------------------------------------------------------------------------------
    //       METODA TWORZACA NOWEGO UZYTKOWNIKA W BAZIE DANYCH

    public User create(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
//            JAK USTAWIC RESULT SET NA PIERWSZY MOZLIWY WIERSZ W TABELI, "ResultSet.TYPE_SCROLL_INSENSITIVE"
//            ALE JAK TO USTAWIC?
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

//       INTERFEJS DO METODY CREATE

//    public static void inputToDatabase() {
//        Scanner scanner = new Scanner(System.in);
//        do {
//            User user = new User();
//            System.out.println("Input username: ");
//            String userString = scanner.nextLine();
//            user.setUserName(userString);
//            System.out.println("Input user email: ");
//            String emailString = scanner.nextLine();
//            user.setEmail(emailString);
//            System.out.println("Input user password: ");
//            String passwordString = scanner.nextLine();
//            user.setPassword(passwordString);
//            System.out.println("Do you want to continue? (Y/N) ");
//            String exit = scanner.nextLine();
//            if (exit.equals("y") || exit.equals("Y") || exit.equals("N") || exit.equals("n")) {
//                if (exit.equals("N") || exit.equals("n")) {
//                    UserDao userDao = new UserDao();
//                    userDao.create(user);
//                    break;
//                } else if (exit.equals("Y") || exit.equals("y")) {
//                    System.out.println("Let's do it again");
//                }
//            } else {
////                TU CHCIALBYM ZEBY PO WYBRANIU INNEJ LITERKI NIZ Y ALBO N ROBIL PETLE I PROSIL O
////                        PODANIE POPRAWNEJ
//                System.out.println(" Input correct answer 'Y' or 'N' ");
//                break;
//            }
//            UserDao userDao = new UserDao();
//            userDao.create(user);
//        } while (true);
//        scanner.close();
//    }

    //---------------------------------------------------------------------------------------------------------

    //        METODA AKTUALIZUJACA UZYTKOWNIKA W BAZIE DANYCH
    public void update(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(UPDATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getUserName());
            statement.setString(3, hashPassword(user.getPassword()));
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//INTERFEJS DO METODY UPDATE
//    public static void updateDatabase() {
//        Scanner scanner = new Scanner(System.in);
//        do {
//            User user = new User();
//            System.out.println("Input user ID: ");
//            while (!scanner.hasNextInt()) {
//                System.out.println("Chosen value is not a number. Please select again: ");
//                scanner.nextLine();
//            }
//            int idInt = scanner.nextInt();
//            user.setId(idInt);
//            scanner.nextLine();
//            System.out.println("Input username: ");
//            String userString = scanner.nextLine();
//            user.setUserName(userString);
//            System.out.println("Input user email: ");
//            String emailString = scanner.nextLine();
//            user.setEmail(emailString);
//            System.out.println("Input user password: ");
//            String passwordString = scanner.nextLine();
//            user.setPassword(passwordString);
//            System.out.println("Do you want to continue? (Y/N) ");
//            String exit = scanner.nextLine();
//            if (exit.equals("y") || exit.equals("Y") || exit.equals("N") || exit.equals("n")) {
//                if (exit.equals("N") || exit.equals("n")) {
//                    UserDao userDao = new UserDao();
//                    userDao.update(user);
//                    break;
//                } else if (exit.equals("Y") || exit.equals("y")) {
//                    System.out.println("Let's do it again");
//                }
//            } else {
////                TU CHCIALBYM ZEBY PO WYBRANIU INNEJ LITERKI NIZ Y ALBO N ROBIL PETLE I PROSIL O
////                        PODANIE POPRAWNEJ
//                System.out.println(" Input correct answer 'Y' or 'N' ");
//                break;
//            }
//            UserDao userDao = new UserDao();
//            userDao.update(user);
//        } while (true);
//        scanner.close();
//    }


    //---------------------------------------------------------------------------------------------
//  WCZYTUJE DO KONSOLI I ZPISUJE INFORMACJE O UZYTKOWNIKU Z BAZY DANYCH
    public User read(int userId) {
        User user = new User();
        Scanner scanner = new Scanner(System.in);
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(READ_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String idString = resultSet.getString(1);//rs.getString("id");
                String email = resultSet.getString(2);//rs.getString("email");
                String username = resultSet.getString(3);//rs.getString("username");
                String password = resultSet.getString(4);//rs.getString("password");
                int id = Integer.parseInt(idString);
                user.setId(id);
                user.setUserName(username);
                user.setEmail(email);
                user.setPassword(password);
//                System.out.println(String.format("Id=%s contains information as follows, username: %s, email: %s, password: %s", id, username, email, password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        scanner.close();
        return user;
    }
//    public User read() {
//        User user = new User();
//        Scanner scanner = new Scanner(System.in);
//        try (Connection conn = DbUtil.getConnection()) {
//            PreparedStatement statement1 =
//                    conn.prepareStatement(SELECT_USER_ID, Statement.RETURN_GENERATED_KEYS);
//            statement1.executeQuery();
//            ResultSet rs = statement1.executeQuery();
//            ResultSetMetaData rsmd = rs.getMetaData();
//            int columnsNumber = rsmd.getColumnCount();
//            System.out.println("Available id numbers are:");
//            while (rs.next()) {
//                for (int i = 1; i <= columnsNumber; i++) {
//                    if (i > 1) System.out.print(",  ");
//                    String columnValue = rs.getString(i);
//                    System.out.print(rsmd.getColumnName(i) + " " + columnValue);
//                }
//                System.out.println("");
//            }
//            System.out.println("Type the ID number of user you want to print information from: ");
//            while (!scanner.hasNextInt()) {
//                System.out.println("Chosen value is not a number. Please select again: ");
//                scanner.nextLine();
//            }
//            int IdNumber = scanner.nextInt();
//            PreparedStatement statement =
//                    conn.prepareStatement(READ_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
//            statement.setInt(1, IdNumber);
//            ResultSet resultSet = statement.executeQuery();
//            while (!resultSet.isBeforeFirst()) {
//                System.out.println("No data in this id, type another number");
//                IdNumber = scanner.nextInt();
//                statement.setInt(1, IdNumber);
//                resultSet = statement.executeQuery();
//            }
//            while (resultSet.next()) {
//                String idString = resultSet.getString(1);//rs.getString("id");
//                String email = resultSet.getString(2);//rs.getString("email");
//                String username = resultSet.getString(3);//rs.getString("username");
//                String password = resultSet.getString(4);//rs.getString("password");
//                int id = Integer.parseInt(idString);
//                user.setId(id);
//                user.setUserName(username);
//                user.setEmail(email);
//                user.setPassword(password);
//                System.out.println(String.format("Id=%s contains information as follows, username: %s, email: %s, password: %s", id, username, email, password));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//        scanner.close();
//        return user;
//    }
//----------------------------------------------------------------------------------------------

    //   USUWA UZYTKOWNIKA Z BAZY DANYCH
    public void delete (int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement =
                    conn.prepareStatement(DELETE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public void delete() {
//        try (Connection conn = DbUtil.getConnection()) {
//            PreparedStatement statement1 =
//                    conn.prepareStatement(SELECT_USER_ID, Statement.RETURN_GENERATED_KEYS);
//            statement1.executeQuery();
//            ResultSet rs = statement1.executeQuery();
//            ResultSetMetaData rsmd = rs.getMetaData();
//            int columnsNumber = rsmd.getColumnCount();
//            System.out.println("Available id numbers are:");
//            while (rs.next()) {
//                for (int i = 1; i <= columnsNumber; i++) {
//                    if (i > 1) System.out.print(",  ");
//                    String columnValue = rs.getString(i);
//                    System.out.print(rsmd.getColumnName(i) + " " + columnValue);
//                }
//                System.out.println("");
//            }
//            Scanner scanner = new Scanner(System.in);
//            System.out.println("Please choose number id to delete: ");
//            int userId = scanner.nextInt();
//            PreparedStatement statement =
//                    conn.prepareStatement(DELETE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
//            statement.setInt(1, userId);
//            statement.executeUpdate();
//            System.out.println(String.format("User id=%s has been successfully deleted", userId));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//---------------------------------------------------------------------------------------------

    //    FINDALL Z LISTĄ

    public User [] findAll() {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(FIND_ALL_USERS_QUERY);
            ResultSet resultSet = statement.executeQuery();
            List<Object> list = new ArrayList<>();
            int counter=0;
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                list.add(counter,user);
                counter++;
            }
            User[] users = new User [list.size()];
            users =list.toArray(users);
//            System.out.println(Arrays.toString(users));
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        } return null;
    }

//    private User[] addToArray(User u, User[] users) {
//        User[] tmpUsers = Arrays.copyOf(users, users.length + 1); // Tworzymy kopię tablicy powiększoną o 1.
//        tmpUsers[users.length] = u; // Dodajemy obiekt na ostatniej pozycji.
//        return tmpUsers; // Zwracamy nową tablicę.
//    }
//
//
//    public User[] findAll() {
//        try (Connection conn = DbUtil.getConnection()) {
//            User[] users = new User[0];
//            PreparedStatement statement = conn.prepareStatement(FIND_ALL_USERS_QUERY);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                User user = new User();
//                user.setId(resultSet.getInt("id"));
//                user.setUserName(resultSet.getString("username"));
//                user.setEmail(resultSet.getString("email"));
//                user.setPassword(resultSet.getString("password"));
//                users = addToArray(user, users);
//            }
//            System.out.println(users[1]);
//            return users;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }


    public static void main(String[] args) {
//        UserDao userDao = new UserDao();
//        METODA TWORZACA NOWEGO UZYTKOWNIKA W BAZIE DANYCH
//        inputToDatabase();
//        METODA AKTUALIZUJACA UZYTKOWNIKA W BAZIE DANYCH
//          updateDatabase();
//        WCZYTUJE DO KONSOLI INFORMACJE O UZYTKOWNIKU Z BAZY DANYCH
//        userDao.read();
//        USUWA UZYTKOWNIKA Z BAZY DANYCH
//        userDao.delete();



    }
}

