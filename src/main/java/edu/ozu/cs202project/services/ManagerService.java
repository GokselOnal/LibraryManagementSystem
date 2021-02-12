package edu.ozu.cs202project.services;
import edu.ozu.cs202project.Salter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
public class ManagerService {

    @Autowired
    JdbcTemplate conn;

    public List<String[]> viewBorrowList(String userId){
        List<String[]> data = conn.query("SELECT * FROM borrow NATURAL JOIN BOOK NATURAL JOIN AUTHOR NATURAL JOIN GENRE WHERE regular_user_id = ?",new String[]{userId},
                (row,index) -> {
                    return new String[] {row.getString("book_id"),row.getString("title"),row.getString("borrow_date"),(row.getString("return_date"))};
                });
        if(data.size() != 0) {
            return data;
        }
        else{
            return null;
        }
    }

    public String totalBorrowCount(String bookId){
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM book WHERE book_id = ?", new Object[]{bookId});
        if(response != null){
            if(response.size() != 0) {
                int count = conn.queryForObject("SELECT COUNT(borrow_id) FROM book NATURAL JOIN borrow WHERE book_id = ?", new Object[]{bookId}, Integer.class);
                String countString = String.valueOf(count);
                return countString;
            }
            else{
                return "0";
            }
        }
        return null;
    }

    public String mostBorrowedGenre(){
        List<Map<String, Object>> response2 = conn.queryForList("SELECT genre_id FROM borrow NATURAL JOIN book GROUP BY genre_id ORDER BY count(genre_id)");
        if(response2.size() != 0) {
            String genreId = conn.queryForObject("SELECT genre_id FROM borrow NATURAL JOIN book GROUP BY genre_id ORDER BY count(genre_id) DESC LIMIT 1",String.class);
            String fav_genre_name = conn.queryForObject("SELECT genre_name FROM genre WHERE genre_id = ?", new Object[]{genreId}, String.class);
            return fav_genre_name;
        }
        return null;
    }

    public String mostBorrowedBookLast3Mount(){
        LocalDate nowDate = LocalDate.now();
        LocalDate threeMounthsBefore = nowDate.minusMonths(3);
        List<Map<String, Object>> response2 = conn.queryForList("SELECT book_id FROM borrow NATURAL JOIN book WHERE borrow_date BETWEEN ? AND ? GROUP BY book_id ORDER BY count(book_id)", new Object[]{threeMounthsBefore,nowDate});
        if(response2.size() != 0) {
            String bookId = conn.queryForObject("SELECT book_id FROM borrow NATURAL JOIN book WHERE borrow_date BETWEEN ? AND ? GROUP BY book_id ORDER BY count(book_id) DESC LIMIT 1",new Object[]{threeMounthsBefore,nowDate}, String.class);
            String most_book_name = conn.queryForObject("SELECT title FROM book WHERE book_id = ?", new Object[]{bookId}, String.class);
            return most_book_name;
        }
        return null;
    }

    public String mostBorrowerPublisher(){
        List<Map<String, Object>> response2 = conn.queryForList("SELECT publisher_id FROM borrow NATURAL JOIN book WHERE borrow_date GROUP BY publisher_id ORDER BY count(publisher_id)");
        if(response2.size() != 0) {
            String publisherId = conn.queryForObject("SELECT publisher_id FROM borrow NATURAL JOIN book WHERE borrow_date GROUP BY publisher_id ORDER BY count(publisher_id) DESC LIMIT 1",String.class);
            String most_borrow_pub = conn.queryForObject("SELECT name FROM users WHERE id = ?", new Object[]{publisherId}, String.class);
            return most_borrow_pub;
        }
        return null;
    }

    public int overdueBookCount(){
        int overdueCount = conn.queryForObject("SELECT COUNT(borrow_id) FROM borrow WHERE overdue = 1",Integer.class);
        return overdueCount;
    }

    public List<String[]> listUsersWhoBorMostBorrowed(){
        List<Map<String, Object>> response2 = conn.queryForList("SELECT book_id FROM borrow NATURAL JOIN book GROUP BY book_id ORDER BY count(book_id)");
        if(response2.size() != 0) {
            String bookId = conn.queryForObject("SELECT book_id FROM borrow NATURAL JOIN book GROUP BY book_id ORDER BY count(book_id) DESC LIMIT 1",String.class);
            //String most_borrow_book = conn.queryForObject("SELECT title FROM book WHERE book_id = ?", new Object[]{bookId}, String.class);
            List<String[]> data = conn.query("SELECT distinct id AS regular_user_id, name FROM users NATURAL JOIN borrow WHERE book_id = ? AND user_type = 1",new String[]{bookId},
                    (row,index) -> {
                        return new String[] {row.getString("regular_user_id"),row.getString("name")};
                    });
            return data;
        }
        return null;

    }

    public String overdueForGivenBook(String title){
        int overdueCount = conn.queryForObject("SELECT COUNT(borrow_id) FROM borrow NATURAL JOIN book WHERE overdue = 1 AND title = ?", new Object[]{title},Integer.class);
        String overdueString = String.valueOf(overdueCount);
        return overdueString;
    }

    public List<String[]> lookForAddRequest(){
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM book WHERE request = ?", new Object[]{"1"});
        if(response.size() != 0) {
            List<String[]> data = conn.query("SELECT * FROM book NATURAL JOIN author NATURAL JOIN  genre WHERE request = ?", new String[]{"1"},
                    (row, index) -> {
                        return new String[]{row.getString("book_id"), row.getString("title"), row.getString("author_name"), row.getString("genre_name"), row.getString("publish_date"), row.getString("page_number"), row.getString("availability")};
                    });
            return data;
        }
        return null;
    }

    public List<String[]> lookForRemRequest() {
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM book WHERE request = ?", new Object[]{"-1"});
        if (response.size() != 0) {
            List<String[]> data = conn.query("SELECT * FROM book NATURAL JOIN author NATURAL JOIN  genre WHERE request = ?", new String[]{"-1"},
                    (row, index) -> {
                        return new String[]{row.getString("book_id"), row.getString("title"), row.getString("author_name"), row.getString("genre_name"), row.getString("publish_date"), row.getString("page_number"), row.getString("availability")};
                    });
            return data;
        }
        return null;
    }

    public int removeBook(String bookID) {
        List<Map<String, Object>> book = conn.queryForList("SELECT book_id FROM book WHERE book_id = ? AND availability = ?", new Object[]{bookID, 1});
        List<Map<String, Object>> book2 = conn.queryForList("SELECT book_id FROM book WHERE book_id = ?", new Object[]{bookID});
        if(book2.size() == 0){
            return -1;
        }
        if (book.size() != 0) {
            conn.update("DELETE FROM book WHERE book_id = ?", new Object[]{bookID});
            return 1;
        }
        else{
            return 0;
        }
    }

    public boolean addBook(String title, String author, String genre, String topic, String publisher, String publishDate, String pageNumber){
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
            String publisherID = conn.queryForObject("SELECT id FROM users WHERE user_type = ? AND name like CONCAT(?,'%')", new Object[]{2,publisher}, String.class);
            conn.update("INSERT INTO book (title,author_id, genre_id, topic_family_id, publisher_id, publish_date, page_number, availability,request) VALUES (?,?,?,?,?,?,?,?,?)", new Object[]{title,authorID,genreID,topicID,publisherID,publishDate,pageNumber,1,0});
            return true;
        }
    }

    public void addPublisher(String id, String password,String name){
        password = Salter.salt(password, "CS202Project");
        conn.update("INSERT INTO users (id,password,name,user_type) values (?,?,?,?)",new Object[]{id,password,name,2});
    }

    public int assignBooktoUser(String userId, String bookId, String date){
        List<Map<String, Object>> response0 = conn.queryForList("SELECT * FROM book WHERE book_id = ? ", new Object[]{bookId});
        List<Map<String, Object>> response1 = conn.queryForList("SELECT * FROM users WHERE id = ? ", new Object[]{userId});
        List<Map<String, Object>> response2 = conn.queryForList("SELECT * FROM book WHERE book_id = ? AND availability = ? ", new Object[]{bookId,1});
        if(response0.size() == 0){
            return 0;
        }
        if(response1.size() == 0){
            return -1;
        }
        if(response2.size() == 0){
            return -2;
        }
        if((response1.size() != 0) && (response2.size() != 0)){

            conn.update("INSERT INTO borrow(regular_user_id,book_id,borrow_date) values (?,?,?)", new Object[]{userId,bookId,date});
            conn.update("UPDATE book SET availability = ? WHERE book_id = ?", new Object[]{0,bookId});
            return 1;
        }
        return -3;
    }

    public int unassignBooktoUser(String userId, String bookId, String date){
        List<Map<String, Object>> response0 = conn.queryForList("SELECT * FROM book WHERE book_id = ? ", new Object[]{bookId});
        List<Map<String, Object>> response1 = conn.queryForList("SELECT * FROM users WHERE id = ? ", new Object[]{userId});
        List<Map<String, Object>> response2 = conn.queryForList("SELECT * FROM book WHERE book_id = ? AND availability = ? ", new Object[]{bookId,0});
        List<Map<String, Object>> response3 = conn.queryForList("SELECT * FROM book NATURAL JOIN borrow WHERE book_id = ? AND regular_user_id = ? AND availability = ? ", new Object[]{bookId,userId,0});
        if(response0.size() == 0){
            return -2;
        }
        if(response1.size() == 0){
            return -1;
        }
        if(response2.size() == 0){
            return -3;
        }
        if(response3.size() == 0){
            return -4;
        }
        if((response3.size() != 0) && (response1.size() != 0)){
            conn.update("UPDATE book SET availability = ? WHERE book_id = ?", new Object[]{1,bookId});
            conn.update("UPDATE borrow SET return_date = ? WHERE book_id = ?", new Object[]{date,bookId});
            return 1;
        }
        return 0;
    }

    public int acceptRequest(String bookId){
        List<Map<String, Object>> response0 = conn.queryForList("SELECT * FROM book WHERE book_id = ? AND availability = ? ", new Object[]{bookId,0});
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM book WHERE book_id = ? AND  request = ?", new Object[]{bookId,1});
        List<Map<String, Object>> response1 = conn.queryForList("SELECT * FROM book WHERE book_id = ? ", new Object[]{bookId});
        List<Map<String, Object>> response2 = conn.queryForList("SELECT * FROM book WHERE book_id = ? AND request = ?", new Object[]{bookId,1});
        if((response.size() != 0) && (response0.size() != 0)){
            conn.update("UPDATE book SET request = ? WHERE book_id = ? ", new Object[]{0,bookId});
            conn.update("UPDATE book SET availability = ? WHERE book_id = ?", new Object[]{1,bookId});
            return 1;
        }
        else if(response1.size() == 0){
            return 0;
        }
        else if(response2.size() == 0){
            return -1;
        }
        return -2;
    }

    public int acceptRemoveReq(String bookId){
        List<Map<String, Object>> response0 = conn.queryForList("SELECT * FROM book WHERE book_id = ? AND availability = ? ", new Object[]{bookId,1});
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM book WHERE book_id = ? AND  request = ?", new Object[]{bookId,-1});
        List<Map<String, Object>> response1 = conn.queryForList("SELECT * FROM book WHERE book_id = ? ", new Object[]{bookId});
        List<Map<String, Object>> response2 = conn.queryForList("SELECT * FROM book WHERE book_id = ? AND request = ?", new Object[]{bookId,-1});
        if((response.size() != 0) && (response0.size() != 0)){
            conn.update("UPDATE book SET request = ? WHERE book_id = ? ", new Object[]{0,bookId});
            conn.update("DELETE FROM book WHERE book_id = ?", new Object[]{bookId});
            return 1;
        }
        else if(response0.size() == 0) {
            return -1;
        }
        else if(response1.size() == 0){
            return 0;
        }
        else if(response2.size() == 0){
            return -2;
        }
        return -3;
    }

    public void checkOverdue(){
        List<Map<String, Object>> response2 = conn.queryForList("SELECT * FROM borrow WHERE overdue = ?", new Object[]{0});
        if(response2.size() == 0){

        }
        for(int i = 0; i < response2.size() ; i++){
            String borrowID = String.valueOf(response2.get(i).get("borrow_id"));
            String userID = String.valueOf(response2.get(i).get("regular_user_id"));
            if(response2.get(i).get("return_date") != null){
                String aa = String.valueOf(response2.get(i).get("return_date"));
                String bb = String.valueOf(response2.get(i).get("borrow_date"));
                LocalDate a1 = LocalDate.parse(aa);
                LocalDate a2 = LocalDate.parse(bb);
                long diff = ChronoUnit.DAYS.between(a2,a1);
                if(diff > 14){
                    List<Map<String, Object>> response3 = conn.queryForList("SELECT * FROM penalty_list WHERE regular_user_id = ?", new Object[]{userID});
                    if(response3.size() != 0){
                        conn.update("UPDATE penalty_list SET debt = debt + ? WHERE regular_user_id = ?", new Object[]{diff-14,userID});
                        conn.update("UPDATE borrow SET overdue = ? WHERE borrow_id = ? ",new Object[]{1,borrowID});
                    }
                    else if(response3.size() == 0) {
                        conn.update("UPDATE borrow SET overdue = ? WHERE borrow_id = ? ",new Object[]{1,borrowID});
                        conn.update("INSERT INTO penalty_list (regular_user_id,debt) VALUES (?,?)", new Object[]{userID, 0});
                        conn.update("UPDATE penalty_list SET debt = debt + ?", new Object[]{diff - 14});
                    }
                }
                else{
                    //conn.update("UPDATE borrow SET overdue = ?", new Object[]{0});
                }
            }
            else{
                String bb = String.valueOf(response2.get(i).get("borrow_date"));
                LocalDate a2 = LocalDate.parse(bb);
                LocalDate dateNow = LocalDate.now();
                long diff = ChronoUnit.DAYS.between(a2,dateNow);
                if(diff > 14){
                    List<Map<String, Object>> response3 = conn.queryForList("SELECT * FROM penalty_list WHERE regular_user_id = ?", new Object[]{userID});
                    if(response3.size() != 0){
                        conn.update("UPDATE penalty_list SET debt = debt + ? WHERE regular_user_id = ?", new Object[]{diff-14,userID});
                        conn.update("UPDATE borrow SET overdue = ? WHERE borrow_id = ? ",new Object[]{1,borrowID});
                    }
                    else if(response3.size() == 0) {
                        conn.update("UPDATE borrow SET overdue = ? WHERE borrow_id = ? ",new Object[]{1,borrowID});
                        conn.update("INSERT INTO penalty_list (regular_user_id,debt) VALUES (?,?)", new Object[]{userID, 0});
                        conn.update("UPDATE penalty_list SET debt = debt + ?", new Object[]{diff - 14});
                    }
                }
                else{
                    //conn.update("UPDATE borrow SET overdue = ?", new Object[]{0});
                }
            }
        }
    }

    public List<String[]> overdueList(){
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM borrow WHERE overdue = ?", new Object[]{1});
        if (response.size() != 0) {
            List<String[]> data = conn.query("SELECT DISTINCT regular_user_id as id ,title FROM users NATURAL JOIN borrow NATURAL JOIN book WHERE user_type = ? AND overdue = ?", new String[]{"1","1"},
                    (row, index) -> {
                        return new String[]{row.getString("id"),(row.getString("title"))};
                    });
            return data;
        }
        return null;
    }

    public List<String[]> historyForInterval(String start, String finish){
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM borrow NATURAL JOIN book WHERE borrow_date BETWEEN ? AND ?", new Object[]{start,finish});
        if(response.size() != 0){
            List<String[]> data = conn.query("SELECT * FROM borrow NATURAL JOIN book WHERE borrow_date BETWEEN ? AND ?", new Object[]{start,finish},
                    (row, index) -> {
                        return new String[]{row.getString("regular_user_id"),(row.getString("book_id"))};
                    });
            return data;
        }
        return null;
    }

    public int overdueCountForTitle(String title){
        int count = conn.queryForObject("SELECT COUNT(borrow_id) FROM borrow NATURAL JOIN book WHERE overdue = ? AND title like CONCAT('%',?,'%')" , new Object[]{1,title}, Integer.class);
        return count;
    }
}
