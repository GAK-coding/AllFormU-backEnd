package gak.backend.domain.description.dao;

import gak.backend.domain.description.model.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class DescriptionJdbcRepository {
    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(List<Description> descriptions){
        log.info("batch Insert들어옴");
        String sql = "INSERT INTO description " +
                "(created_date, modified_date, content, member_id, question_id) VALUES (?, ?, ?, ?, ?)";
        log.info("before update");

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                log.info("set에 들어옴");
                //ps.setLong(1, descriptions.get(i).getId());
                ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(3, descriptions.get(i).getContent());
                ps.setLong(4, descriptions.get(i).getMember().getId());
                ps.setLong(5, descriptions.get(i).getQuestion().getId());
                log.info("insert 성공");
            }

            @Override
            public int getBatchSize() {
                return descriptions.size();
            }
        });
    }
}
