
package org.meklu.patkis.domain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Handles database things
 */
public class Database {
    private final String uri;
    private final Connection conn;

    /** Opens a database connection
     * 
     * @param filename SQLite database path
     * @throws SQLException 
     */
    public Database(String filename) throws SQLException {
        this.uri = "jdbc:sqlite:" + filename;
        this.conn = DriverManager.getConnection(uri);
        this.conn.prepareStatement("PRAGMA foreign_keys = ON").execute();
    }

    /** Gets the connection associated with this object
     *
     * @see java.sql.Connection
     * @return Connection
     */
    public Connection getConn() {
        return this.conn;
    }

    /** Closes the connection associated with this object
     *
     * @see java.sql.Connection
     */
    public void close() {
        try {
            this.conn.close();
        } catch (SQLException ex) {
            System.err.println("failed to close database connection");
        }
    }
    
    /** Starts a transaction
     */
    public void startTransaction() {
        try {
            this.conn.setAutoCommit(false);
        } catch (Exception e) {}
    }

    /** Commits a transaction
     */
    public void commit() {
        try {
            this.conn.commit();
            this.conn.setAutoCommit(true);
        } catch (Exception e) {}
    }

    /** Rolls back a transaction
     */
    public void rollback() {
        try {
            this.conn.rollback();
            this.conn.setAutoCommit(true);
        } catch (Exception e) {}
    }

    /** Gets the id of the last inserted row
     *
     * @return The id of the last inserted row
     */
    public int lastId(String table) {
        try {
            PreparedStatement stmt = this.conn.prepareStatement("SELECT seq FROM sqlite_sequence WHERE name = ?");
            stmt.setString(1, table);
            ResultSet res = stmt.executeQuery();
            if (!res.next()) {
                return -1;
            }
            return res.getInt("seq");
        } catch (Exception e) {}
        return -1;
    }

    /** Private utility function that reads a file as a string
     *
     * @param file The path to the file being read
     * @return The file contents
     */
    private String readFile(String file) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        return br.lines().reduce("", (a, b) -> a + "\n" + b);
    }

    /** A hacky private utility function that executes an SQL file
     * that only contains DDL statements
     *
     * @param file The path to the executed file
     */
    private void execFile(String file) throws SQLException, FileNotFoundException {
        this.startTransaction();
        String[] parts = readFile(file).split(";");
        for (String p : parts) {
            String q = p.trim();
            Statement t = this.conn.createStatement();
            t.closeOnCompletion();
            t.executeUpdate(q);
        }
        this.commit();
    }

    /** Finds rows where field = value
     *
     * @param table The table to query
     * @param field The field to check against
     * @param value The value to compare the field to
     * @return Possibly a ResultSet
     */
    public ResultSet find(String table, String field, String value) {
        List<Triple<String, String, String>> l = new ArrayList<>();
        l.add(new Triple(field, "=", value));
        return findWhere(table, l);
    }

    /** Finds rows where field matches the given SQL pattern
     *
     * @param table The table to query
     * @param field The field to check against
     * @param pattern The value to compare the field to
     * @return Possibly a ResultSet
     */
    public ResultSet findLike(String table, String field, String pattern) {
        try {
            List<Triple<String, String, String>> l = new ArrayList<>();
            l.add(new Triple(field, "LIKE", pattern));
            return this.findWhere(table, l);
        } catch (Exception e) {}
        return null;
    }

    /** Finds rows where the given fields match the given values according to the provided operator
     *
     * @param table The table to query
     * @param whereFields The fields to check against in (field, operator, value) form, e.g. ("name", "LIKE", "%ant%")
     * @param additionalOrders Additional SQL command structuring after the WHERE clause
     * @return Possibly a ResultSet
     */
    public ResultSet findWhere(String table, List<Triple<String, String, String>> whereFields, List<String> additionalOrders) {
        try {
            String frepl = String.join(",", whereFields.stream().map(f -> f.getA() + " " + f.getB() + " ?").reduce("", (a, b) -> a + b));
            String addtl = String.join(" ", additionalOrders);
            String query = "SELECT * FROM " + table + (!whereFields.isEmpty() ? " WHERE " + frepl : "") + " " + addtl;
            PreparedStatement stmt = conn.prepareStatement(query);
            for (int i = 1; i <= whereFields.size(); ++i) {
                Triple<String, String, String> t = whereFields.get(i - 1);
                stmt.setString(i, t.getC());
            }
            return stmt.executeQuery();
        } catch (Exception e) {}
        return null;
    }

    /** Finds rows where the given fields match the given values according to the provided operator
     *
     * @param table
     * @param whereFields
     * @return Possibly a ResultSet
     */
    public ResultSet findWhere(String table, List<Triple<String, String, String>> whereFields) {
        return this.findWhere(table, whereFields, new ArrayList<>());
    }

    /** Saves a row
     *
     * @param table The table to insert into
     * @param fields A list of pairs of (field, value)
     * @return Whether we succeeded or not
     */
    public boolean save(String table, List<Pair<String, String>> fields) {
        try {
            String frepl = String.join(",", fields.stream().map(f -> "?").reduce("", (a, b) -> a + b));
            String fnames = String.join(",", fields.stream().map(f -> f.getA()).reduce("", (a, b) -> a + b));
            String query = "INSERT INTO " + table + " (" + fnames + ") VALUES (" + frepl + ")";
            PreparedStatement stmt = conn.prepareStatement(query);
            for (int i = 0; i < fields.size(); ++i) {
                int off = 1;
                stmt.setString(off + i, fields.get(i).getB());
            }
            stmt.execute();
            return stmt.getUpdateCount() > 0;
        } catch (Exception e) {}
        return false;
    }

    /** Saves a row but returns the inserted item's id as well
     *
     * @param table The table to insert into
     * @param fields A list of pairs of (field, value)
     * @return The id of the newly inserted row
     */
    public int saveReturningId(String table, List<Pair<String, String>> fields) {
        try {
            this.startTransaction();
            boolean ret = this.save(table, fields);
            int id = this.lastId(table);
            this.commit();
            if (!ret) {
                return -1;
            }
            return id;
        } catch (Exception e) {}
        return -1;
    }

    /** Deletes a row
     *
     * @param table The table to delete from
     * @param field The field to delete by
     * @param value The value to delete by
     * @return Whether we succeeded
     */
    public boolean delete(String table, String field, String value) {
        try {
            ArrayList<Pair<String, String>> l = new ArrayList<>();
            l.add(new Pair(field, value));
            return this.delete(table, l);
        } catch (Exception e) {}
        return false;
    }

    /** Deletes a row
     *
     * @param table The table to delete from
     * @param whereFields The fields according to which the row is deleted as (key, value) pairs. These are ANDed.
     * @return Whether we succeeded
     */
    public boolean delete(String table, List<Pair<String, String>> whereFields) {
        try {
            String frepl = String.join(" AND ", whereFields.stream().map(f -> f.getA() + "= ?").reduce("", (a, b) -> a + b));
            String query = "DELETE FROM " + table + " WHERE " + frepl;
            PreparedStatement stmt = conn.prepareStatement(query);
            for (int i = 1; i <= whereFields.size(); ++i) {
                Pair<String, String> p = whereFields.get(i - 1);
                stmt.setString(i, p.getB());
            }
            stmt.execute();
            return stmt.getUpdateCount() > 0;
        } catch (Exception e) {}
        return false;
    }

    /** Drops all the tables in the database
     *
     * CAUTION: Removes everything!
     */
    public void dropTables() {
        try {
            this.execFile("sql/drop_tables.sql");
        } catch (Exception e) {}
    }

    /** Re-creates all the tables in the database, if necessary
     */
    public void createTables() {
        try {
            this.execFile("sql/create_tables.sql");
        } catch (Exception e) {}
    }

    /** Resets the database
     *
     * CAUTION: Drops all tables and re-creates them
     */
    public void reset() {
        this.dropTables();
        this.createTables();
    }
}
