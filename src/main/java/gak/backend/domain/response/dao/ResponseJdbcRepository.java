package gak.backend.domain.response.dao;

import gak.backend.domain.response.model.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ResponseJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(List<Response> responses){
        log.info("enter");
        String sql = "INSERT INTO response" +
                "(created_date, modified_date, num, question_id, member_id) VALUES(?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                log.info("set");
                log.info(responses.get(i).getQuestion().getId()+"객체");
                log.info(responses.get(i).getResponsor().getId()+"객체");
                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                ps.setInt(3, responses.get(i).getNum());
                ps.setLong(4, responses.get(i).getResponsor().getId());
                ps.setLong(5, responses.get(i).getQuestion().getId());
                log.info("fin");
            }

            @Override
            public int getBatchSize() {
                return responses.size();
            }
        });
        log.info("jjin fin");
    }


}
