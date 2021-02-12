package edu.ozu.cs202project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SignUpService {

    @Autowired
    JdbcTemplate conn;

    public void signUp(String userId, String password,String username){
        conn.update("INSERT INTO USERS (id,name,password,user_type) VALUES ( ?,?,?,1)",new Object[] {userId,username,password});
    }

    public boolean validate(String userId){
        List<Map<String, Object>> response = conn.queryForList("SELECT * FROM users WHERE id = ?", new Object[] {userId});
        return response.size() == 1;
    }
}
