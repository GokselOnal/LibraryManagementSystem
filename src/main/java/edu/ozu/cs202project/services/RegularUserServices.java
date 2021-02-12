package edu.ozu.cs202project.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import javax.swing.tree.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class RegularUserServices {

    @Autowired
    JdbcTemplate conn;

    public List<String[]> searchBook(String input){
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM book NATURAL JOIN author NATURAL JOIN  genre WHERE title  like  CONCAT('%',?,'%') OR author_name like  CONCAT('%',?,'%') OR genre_name like  CONCAT('%',?,'%')",new Object[]{input,input,input});
        if(response.size() != 0){
            List<String[]> data = conn.query("SELECT * FROM book NATURAL JOIN author NATURAL JOIN  genre NATURAL JOIN section WHERE request = ? AND title  like  CONCAT('%',?,'%') OR author_name like  CONCAT('%',?,'%') OR genre_name like  CONCAT('%',?,'%')", new String[]{"0",input,input,input},
                    (row,index) -> {
                        return new String[] {row.getString("book_id"),row.getString("title"), row.getString("author_name"), row.getString("genre_name"), row.getString("publish_date"),row.getString("page_number"),row.getString("availability"), row.getString("floor_number"), row.getString("part_name")};
                    });
            return data;
        }
        return null;
    }

    public int borrowBook(String userId, String bookId, String date){
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM book WHERE book_id = ?",new Object[]{bookId});
        if(response.size() == 0 ){
            return -1;
        }
        String availabilityStatue = conn.queryForObject("SELECT availability FROM book WHERE book_id = ?", new Object[]{bookId}, String.class);
        if(availabilityStatue.equals("0")){
            conn.update("INSERT INTO hold_request (regular_user_id,book_id,request_date) values (?,?,?)", new Object[]{userId, bookId, date});
            return 0;
        }
        else {
            conn.update("INSERT INTO borrow (regular_user_id,book_id,borrow_date) values (?,?,?)", new Object[]{userId, bookId, date});
            conn.update("UPDATE book SET availability = 0 WHERE book_id = ?", new Object[]{bookId});
            conn.update("UPDATE borrow SET overdue = 0 WHERE book_id = ?", new Object[]{bookId});
            return 1;
        }
    }

    public boolean returnBook(String bookId,String date){
        List<Map<String, Object>> response0 = conn.queryForList("SELECT borrow_id FROM borrow WHERE return_date is null AND book_id = ?", new Object[]{bookId});
        if(response0.size() == 0){
            return false;
        }
        else {
            conn.update("UPDATE borrow SET return_date = ? WHERE book_id = ?", new Object[]{date, bookId});
            List<Map<String, Object>> response = conn.queryForList("SELECT regular_user_id FROM hold_request WHERE book_id = ?", new Object[] {bookId});
            if (response.size() == 0){
                conn.update("UPDATE book SET availability = 1 WHERE book_id = ?", new Object[]{bookId});
            }
            else if(response.size() != 0){
                String nextHoldUser = conn.queryForObject("SELECT regular_user_id FROM hold_request WHERE book_id = ? LIMIT 1", new Object[] {bookId}, String.class);
                conn.update("INSERT INTO borrow (regular_user_id,book_id,borrow_date,return_date) values (?,?,?,?)",new Object[]{nextHoldUser,bookId,date,null});
                conn.update("DELETE FROM hold_request WHERE regular_user_id = ? AND book_id = ?", new Object[]{nextHoldUser,bookId});
            }
            return true;
        }
    }

    public List<String[]> userHistory(String userId){
        List<String[]> data = conn.query("SELECT * FROM borrow NATURAL JOIN BOOK NATURAL JOIN AUTHOR NATURAL JOIN GENRE NATURAL JOIN section WHERE regular_user_id = ?",new String[]{userId},
                (row,index) -> {
                    return new String[] {row.getString("book_id"),row.getString("title"), row.getString("author_name"), row.getString("genre_name"), row.getString("publish_date"), row.getString("return_date"),row.getString("page_number"),row.getString("availability"), row.getString("floor_number"), row.getString("part_name")};
                });
        return data;
    }

    public List<String[]> searchBookForTopic(String topicName){
        List<Map<String, Object>> response = conn.queryForList("SELECT DISTINCT topic_name FROM book NATURAL JOIN author NATURAL JOIN genre NATURAL JOIN topic WHERE topic_name like  CONCAT('%',?,'%')", new Object[]{topicName});
        ArrayList<Integer> relTopic = new ArrayList<Integer>();
        for(int i = 0 ; i < response.size(); i++){
            String a = (String)response.get(i).get("topic_name");
            List<Map<String, Object>> ids = conn.queryForList("SELECT topic_family_id FROM topic WHERE topic_name = ? ",new Object[]{a});
            for(int j = 0 ; j < ids.size() ; j++){
                int ids2 = (Integer)ids.get(j).get("topic_family_id");
                relTopic.add(ids2);
            }
        }
        if (response.size() != 0) {
            List<String[]> data = conn.query("SELECT * FROM book NATURAL JOIN author NATURAL JOIN  genre NATURAL JOIN topic NATURAL JOIN section WHERE request = ? AND topic_family_id = ?", new Integer[]{0,relTopic.get(0)},
                    (row, index) -> {
                        return new String[] {row.getString("book_id"),row.getString("title"), row.getString("author_name"), row.getString("genre_name"), row.getString("publish_date"),row.getString("page_number"),row.getString("availability"), row.getString("floor_number"), row.getString("part_name")};
                    });
            for(int i = 1 ; i < relTopic.size() ; i++) {
                List<String[]> data2 = conn.query("SELECT * FROM book NATURAL JOIN author NATURAL JOIN  genre NATURAL JOIN topic NATURAL JOIN section WHERE request = ? AND  topic_family_id = ?", new Integer[]{0,relTopic.get(i)},
                        (row, index) -> {
                            return new String[] {row.getString("book_id"),row.getString("title"), row.getString("author_name"), row.getString("genre_name"), row.getString("publish_date"),row.getString("page_number"),row.getString("availability"), row.getString("floor_number"), row.getString("part_name")};
                        });
                data.addAll(data2);
            }
            return data;
        }
        return null;
    }

    public int totNumBook(String userId){
        int count = conn.queryForObject("SELECT COUNT(borrow_id) FROM borrow WHERE regular_user_id = ?" , new Object[]{userId}, Integer.class);
        return count;
    }

    public String favourite_genre(String userId){
        List<Map<String, Object>> response = conn.queryForList("SELECT genre_id FROM borrow NATURAL JOIN section NATURAL JOIN book WHERE  regular_user_id = ? GROUP BY genre_id ORDER BY count(genre_id) DESC",new Object[]{userId});
        if(response.size() != 0){
            String genreId = conn.queryForObject("SELECT genre_id FROM borrow NATURAL JOIN book WHERE  regular_user_id = ? GROUP BY genre_id ORDER BY count(genre_id) DESC LIMIT 1",new Object[] {userId},String.class);
            String fav_genre_name = conn.queryForObject("SELECT genre_name FROM genre WHERE genre_id = ?", new Object[]{genreId}, String.class);
            return fav_genre_name;
        }
        return null;
    }

    public List<String[]> searchBookForAuthor(String author_name) {
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM book NATURAL JOIN author NATURAL JOIN  genre WHERE  author_name like  CONCAT('%',?,'%')", new Object[]{author_name});
        if (response.size() != 0) {
            List<String[]> data = conn.query("SELECT * FROM book NATURAL JOIN author NATURAL JOIN  genre NATURAL JOIN section WHERE request = ? AND author_name like  CONCAT('%',?,'%')", new String[]{"0",author_name},
                    (row, index) -> {
                        return new String[] {row.getString("book_id"),row.getString("title"), row.getString("author_name"), row.getString("genre_name"), row.getString("publish_date"),row.getString("page_number"),row.getString("availability"), row.getString("floor_number"), row.getString("part_name")};
                    });
            return data;
        }
        return null;
    }

    public List<String[]> searchBookForGenre(String genreName) {
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM book NATURAL JOIN author  NATURAL JOIN  genre WHERE  genre_name like  CONCAT('%',?,'%')", new Object[]{genreName});
        if (response.size() != 0) {
            List<String[]> data = conn.query("SELECT * FROM book NATURAL JOIN author NATURAL JOIN  genre NATURAL JOIN section WHERE request = ? AND genre_name like  CONCAT('%',?,'%')", new String[]{"0",genreName},
                    (row, index) -> {
                        return new String[] {row.getString("book_id"),row.getString("title"), row.getString("author_name"), row.getString("genre_name"), row.getString("publish_date"),row.getString("page_number"),row.getString("availability"), row.getString("floor_number"), row.getString("part_name")};
                    });
            return data;
        }
        return null;
    }

    public List<String[]> searchBookForPublisher(String publisherName) {
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM book NATURAL JOIN author  NATURAL JOIN  genre NATURAL JOIN section WHERE publisher_id =(select id from users where name like CONCAT('%',?,'%'));", new Object[]{publisherName});
        if (response.size() != 0) {
            List<String[]> data = conn.query("SELECT * FROM book NATURAL JOIN author  NATURAL JOIN  genre NATURAL JOIN section WHERE request = ? AND publisher_id =(select id from users where name like CONCAT('%',?,'%'));", new Object[]{"0",publisherName},
                    (row, index) -> {
                        return new String[] {row.getString("book_id"),row.getString("title"), row.getString("author_name"), row.getString("genre_name"), row.getString("publish_date"),row.getString("page_number"),row.getString("availability"), row.getString("floor_number"), row.getString("part_name")};
                    });
            return data;
        }
        return null;
    }

    public List<String[]> searchBookForAvailable() {
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM book NATURAL JOIN author NATURAL JOIN  genre WHERE availability = ?", new Object[]{1});
        if (response.size() != 0) {
            List<String[]> data = conn.query("SELECT * FROM book NATURAL JOIN author NATURAL JOIN genre NATURAL JOIN section WHERE request = ? AND availability = ?", new String[]{"0","1"},
                    (row, index) -> {
                        return new String[] {row.getString("book_id"),row.getString("title"), row.getString("author_name"), row.getString("genre_name"), row.getString("publish_date"),row.getString("page_number"),row.getString("availability"), row.getString("floor_number"), row.getString("part_name")};
                    });
            return data;
        }
        return null;
    }

    public List<String[]> searchBookForYear(String year) {
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM book NATURAL JOIN author NATURAL JOIN  genre  WHERE  publish_date like  CONCAT('%',?,'%')", new Object[]{year});
        if (response.size() != 0) {
            List<String[]> data = conn.query("SELECT * FROM book NATURAL JOIN author NATURAL JOIN  genre NATURAL JOIN section WHERE request = ? AND publish_date like  CONCAT('%',?,'%')", new Object[]{"0",year},
                    (row, index) -> {
                        return new String[] {row.getString("book_id"),row.getString("title"), row.getString("author_name"), row.getString("genre_name"), row.getString("publish_date"),row.getString("page_number"),row.getString("availability"), row.getString("floor_number"), row.getString("part_name")};
                    });
            return data;
        }
        return null;
    }

    public List<String[]> searchBookForTitle(String title) {
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM book NATURAL JOIN author NATURAL JOIN  genre  WHERE  title like  CONCAT('%',?,'%')", new Object[]{title});
        if (response.size() != 0) {
            List<String[]> data = conn.query("SELECT * FROM book NATURAL JOIN author NATURAL JOIN  genre NATURAL JOIN section WHERE request = ? AND title like  CONCAT('%',?,'%')", new Object[]{"0",title},
                    (row, index) -> {
                        return new String[] {row.getString("book_id"),row.getString("title"), row.getString("author_name"), row.getString("genre_name"), row.getString("publish_date"),row.getString("page_number"),row.getString("availability"), row.getString("floor_number"), row.getString("part_name")};
                    });
            return data;
        }
        return null;
    }

    public ArrayList<String> searchPublisherForGenre(String genre) {
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM book NATURAL JOIN author NATURAL JOIN  genre  WHERE  genre_name like  CONCAT('%',?,'%')", new Object[]{genre});
        if(response.size() != 0) {
            List<Map<String, Object>> response2 = conn.queryForList("SELECT distinct publisher_id as id FROM book NATURAL JOIN users NATURAL JOIN  genre WHERE user_type = 2 AND request = ? AND genre_name like CONCAT('%',?,'%')", new Object[]{"0",genre});
            ArrayList<String> al = new ArrayList<String>();
            for(int i = 0; i < response2.size(); i++){
               String pubName = conn.queryForObject("SELECT name FROM users WHERE id = ?", new Object[]{response2.get(i).get("id")}, String.class);
                al.add(pubName);
            }
            return al;
        }
        return null;
    }

    public int overdueCount(String userID){
        int count = conn.queryForObject("SELECT COUNT(borrow_id) FROM borrow WHERE overdue = ? AND regular_user_id = ?" , new Object[]{1,userID}, Integer.class);
        return count;
    }
}