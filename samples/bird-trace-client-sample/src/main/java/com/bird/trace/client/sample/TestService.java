package com.bird.trace.client.sample;

import com.bird.trace.client.aspect.Traceable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * @author liuxx
 * @date 2019/8/6
 */
@Service
public class TestService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @Traceable(value = "trace-service")
    public int test(String p1, String p2) {
        int result = jdbcTemplate.update("insert into `zero_user_claim`(`userId`,`key`,`value`) values (?,?,?)", 1, p1, p2);
        return result;
    }
}
