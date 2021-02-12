package edu.ozu.cs202project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class PublisherServices {

    @Autowired
    JdbcTemplate conn;

    public List<String[]> viewBooks(){
        List<String[]> data = conn.query("SELECT * FROM book NATURAL JOIN author NATURAL JOIN  genre NATURAL JOIN section",
                (row,index) -> {
                    return new String[] {row.getString("book_id"),row.getString("title"), row.getString("author_name"), row.getString("genre_name"), row.getString("publish_date"),row.getString("page_number"),row.getString("availability"), row.getString("floor_number"), row.getString("part_name")};
                });
        return data;
    }

    public int borrowCount(String publisherId){
        int count = conn.queryForObject("SELECT COUNT(borrow_id) FROM book NATURAL JOIN borrow WHERE publisher_id = ?" , new Object[]{publisherId}, Integer.class);
        return count;
    }

    public boolean addRequest(String title, String author, String genre, String topic, String publisher, String publishDate, String pageNumber){
        List<Map<String, Object>> response0 = conn.queryForList("SELECT * FROM book NATURAL JOIN author WHERE title = ? AND author_name = ?", new Object[]{title,author});
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM author WHERE author_name = ?", new Object[]{author});
        List<Map<String, Object>> response2 = conn.queryForList("SELECT * FROM genre WHERE genre_name = ?", new Object[]{genre});
        List<Map<String, Object>> response3 = conn.queryForList("SELECT * FROM topic WHERE topic_name = ?", new Object[]{topic});
        String authorID, genreID, topicID;
        if(response0.size() != 0){
            return false;
        }
        else {
            if(response.size() == 0) {
                conn.update("INSERT INTO author(author_name) VALUES(?)", new Object[]{author});
                authorID = conn.queryForObject("SELECT author_id FROM author WHERE author_name = ?", new Object[]{author}, String.class);
            }
            else{
                authorID = conn.queryForObject("SELECT author_id FROM author WHERE author_name = ?", new Object[]{author}, String.class);
            }
            if(response2.size() == 0) {
                conn.update("INSERT INTO genre(genre_name) VALUES(?)", new Object[]{genre});
                genreID = conn.queryForObject("SELECT genre_id FROM genre WHERE genre_name = ?", new Object[]{genre}, String.class);
            }
            else{
                genreID = conn.queryForObject("SELECT genre_id FROM genre WHERE genre_name = ?", new Object[]{genre}, String.class);
            }
            if(response3.size() == 0){
                conn.update("INSERT INTO topic(topic_name) VALUES(?)", new Object[]{topic});
                topicID = conn.queryForObject("SELECT topic_family_id FROM topic WHERE topic_name = ?", new Object[]{topic}, String.class);
            }
            else{
                topicID = conn.queryForObject("SELECT topic_family_id FROM topic WHERE topic_name = ? LIMIT 1", new Object[]{topic}, String.class);
            }
            conn.update("INSERT INTO BOOK (title,author_id, genre_id, topic_family_id, publisher_id, publish_date, page_number, availability,request) VALUES (?,?,?,?,?,?,?,?,?)", new Object[]{title,authorID,genreID,topicID,publisher,publishDate,pageNumber,0,1});
            return true;
        }
    }

    public int removeRequest(String title, String author, String publisher) {
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM author WHERE author_name = ?", new Object[]{author});
        List<Map<String, Object>> response2 = conn.queryForList("SELECT * FROM book WHERE title = ?", new Object[]{title});
        List<Map<String, Object>> response4 = conn.queryForList("SELECT book.publisher_id FROM users NATURAL JOIN book WHERE id = ? AND title = ?", new Object[]{publisher,title});
        List<Map<String, Object>> response3 = conn.queryForList("SELECT * FROM book NATURAL JOIN author WHERE title = ? AND author_name = ?", new Object[]{title,author});
        if(response4.size() == 0){
            return -5;
        }
        else if(response2.size() == 0){
            return -1;
        }
        else if(response.size() == 0){
            return -2;
        }
        else if(response3.size() == 0){
            return -3;
        }
        else if((response3.size() != 0) &&(response4.size() != 0)) {
                String authorID = conn.queryForObject("SELECT author_id FROM author WHERE author_name = ?", new Object[]{author}, String.class);
                conn.update("UPDATE BOOK SET request = ? WHERE title = ? AND  author_id = ? AND publisher_id = ?", new Object[]{-1, title, authorID, publisher});
                return 1;
            }
        return -4;
    }
}
