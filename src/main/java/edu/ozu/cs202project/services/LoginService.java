package edu.ozu.cs202project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class LoginService {

    @Autowired
    JdbcTemplate conn;

    public boolean validate(String username, String password) {
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM users WHERE id = ? AND password = ?", new Object[] {username, password});
        return response.size() == 1;
    }

    public String getType(String userId) {
        String result = conn.queryForObject("SELECT user_type FROM users WHERE id = ?", new Object[]{userId}, String.class);
        return result;
    }

    public String getName(String userId) {
        List<Map<String, Object>> response = conn.queryForList("SELECT name FROM users WHERE id = ?", new Object[]{userId});
        if(response.size() == 0){
            return null;
        }
        else{
            String result = conn.queryForObject("SELECT name FROM users WHERE id = ?", new Object[]{userId}, String.class);
            return result;
        }
    }
}
