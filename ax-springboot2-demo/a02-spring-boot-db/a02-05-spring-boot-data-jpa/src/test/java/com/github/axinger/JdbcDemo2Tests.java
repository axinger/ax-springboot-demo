package com.github.axinger;

import com.github.axinger.entity.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@SpringBootTest
public class JdbcDemo2Tests {

    //        JdbcTemplate 中execute和update的区别：
//        execute不接受参数，无返回值，适用于create和drop table。
//        update可以接受参数，返回值为此次操作影响的记录数，适合于insert, update, 和delete等操作。

    @Autowired
    private JdbcTemplate jdbcTemplate;


    //jdbcTemplate.update适合于insert 、update和delete操作；

    /**
     * 第一个参数为执行sql
     * 第二个参数为参数数据
     */
    @Test
    public void save3() {
        SysUser sysUser = new SysUser();
        sysUser.setUsername("jim");
        sysUser.setPassword("123");
        System.out.println("user = " + sysUser);

        Assert.notNull(sysUser, "user is not null");
        jdbcTemplate.update("insert into tb_user(name,password) values(?,?)",
                sysUser.getUsername(), sysUser.getPassword());
    }

    @Test
    public void 新增2() {
        int update = jdbcTemplate.update("insert into tb_user(name,password) values('lili','22222')");
        System.out.println("update = " + update);
    }

    /**
     * 第一个参数为执行sql
     * 第二个参数为参数数据
     * 第三个参数为参数类型
     */
    @Test
    public void save(SysUser sysUser) {
        Assert.notNull(sysUser, "user is not null");
        jdbcTemplate.update(
                "insert into tb_user(name,password) values(?,?)",
                new Object[]{sysUser.getUsername(), sysUser.getPassword()},
                new int[]{java.sql.Types.VARCHAR, java.sql.Types.VARCHAR}
        );
    }

    //避免sql注入
    @Test
    public void save2() {
        SysUser sysUser = new SysUser();
        sysUser.setUsername("tom");
        sysUser.setPassword("123");
        System.out.println("user = " + sysUser);

        Assert.notNull(sysUser, "user is not null");

        jdbcTemplate.update("insert into tb_user (name,password) values(?,?)",
                ps -> {
                    ps.setString(1, sysUser.getUsername());
                    ps.setString(2, sysUser.getPassword());
                });

    }

    public void save4(SysUser sysUser) {
        Assert.notNull(sysUser, "user is not null");
        jdbcTemplate.update("insert into tb_user(name,password) values(?,?)",
                new Object[]{sysUser.getUsername(), sysUser.getPassword()});
    }

    //返回插入的主键
    public List save5(final SysUser sysUser) {

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement("insert into tb_user(name,password) values(?,?)", new String[]{"id"});
                    ps.setString(1, sysUser.getUsername());
                    ps.setString(2, sysUser.getPassword());
                    return ps;
                },
                keyHolder);

        return keyHolder.getKeyList();
    }


    public void update(final SysUser sysUser) {
        jdbcTemplate.update(
                "update tb_user set name=?,password=? where id = ?",
                ps -> {
                    ps.setString(1, sysUser.getUsername());
                    ps.setString(2, sysUser.getPassword());
                    ps.setLong(3, sysUser.getId());
                }
        );
    }


    public void delete(SysUser sysUser) {
        Assert.notNull(sysUser, "user is not null");
        jdbcTemplate.update(
                "delete from tb_user where id = ?",
                new Object[]{sysUser.getId()},
                new int[]{java.sql.Types.INTEGER});
    }

    @Deprecated //因为没有查询条件，所以用处不大
    public Integer queryForInt1() {

//        return jdbcTemplate.queryForInt("select count(0) from tb_user");
        return jdbcTemplate.queryForObject("select count(0) from tb_user", Integer.class);

    }

    public Integer queryForInt2(SysUser sysUser) {
        return jdbcTemplate.queryForObject("select count(0) from tb_user where username = ?",
                new Object[]{sysUser.getUsername()}, Integer.class);

    }

    //最全的参数3个
    public Integer queryForInt3(SysUser sysUser) {
        return jdbcTemplate.queryForObject("select count(0) from tb_user where username = ?",
                new Object[]{sysUser.getUsername()},
                new int[]{java.sql.Types.VARCHAR}, Integer.class);
    }

    //可以返回是一个基本类型的值
    @Deprecated  //因为没有查询条件，所以用处不大
    public String queryForObject1(SysUser sysUser) {
        return (String) jdbcTemplate.queryForObject("select username from tb_user where id = 100",
                String.class);
    }

    //可以返回值是一个对象
    @Deprecated //因为没有查询条件，所以用处不大
    public SysUser queryForObject2(SysUser sysUser) {
        return (SysUser) jdbcTemplate.queryForObject("select * from tb_user where id = 100", SysUser.class); //class是结果数据的java类型
    }

    @Deprecated //因为没有查询条件，所以用处不大
    public SysUser queryForObject3(SysUser sysUser) {
        return (SysUser) jdbcTemplate.queryForObject("select * from tb_user where id = 100",
                new RowMapper() {

                    @Override
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        SysUser sysUser = new SysUser();
                        sysUser.setId(rs.getLong("id"));
                        sysUser.setUsername(rs.getString("username"));
                        sysUser.setPassword(rs.getString("password"));
                        return sysUser;
                    }
                }
        );
    }

    public SysUser queryForObject4(SysUser sysUser) {
        return (SysUser) jdbcTemplate.queryForObject("select * from tb_user where id = ?",
                new Object[]{sysUser.getId()},
                SysUser.class); //class是结果数据的java类型  实际上这里是做反射，将查询的结果和User进行对应复制
    }

    public SysUser queryForObject5(SysUser sysUser) {
        return (SysUser) jdbcTemplate.queryForObject(
                "select * from tb_user where id = ?",
                new Object[]{sysUser.getId()},
                new RowMapper() {

                    @Override
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        SysUser sysUser = new SysUser();
                        sysUser.setId(rs.getLong("id"));
                        sysUser.setUsername(rs.getString("username"));
                        sysUser.setPassword(rs.getString("password"));
                        return sysUser;
                    }

                }); //class是结果数据的java类型
    }


    public SysUser queryForObject(SysUser sysUser) {
        //方法有返回值
        return (SysUser) jdbcTemplate.queryForObject("select * from tb_user where id = ?",
                new Object[]{sysUser.getId()},
                new int[]{java.sql.Types.INTEGER},
                new RowMapper() {

                    @Override
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        SysUser sysUser = new SysUser();
                        sysUser.setId(rs.getLong("id"));
                        sysUser.setUsername(rs.getString("username"));
                        sysUser.setPassword(rs.getString("password"));
                        return sysUser;
                    }
                }
        );
    }

    @SuppressWarnings("unchecked")
    public List<SysUser> queryForList1(SysUser sysUser) {
        return (List<SysUser>) jdbcTemplate.queryForList("select * from tb_user where username = ?",
                new Object[]{sysUser.getUsername()},
                SysUser.class);
    }

//    @SuppressWarnings("unchecked")
//    public List<String> queryForList2(User user) {
//        return (List<String>) jdbcTemplate.queryForList("select username from tb_user where sex = ?",
//                new Object[]{user.getSex()},
//                String.class);
//    }

    @SuppressWarnings("unchecked")
//最全的参数查询
    public List<SysUser> queryForList3(SysUser sysUser) {
        return (List<SysUser>) jdbcTemplate.queryForList("select * from tb_user where username = ?",
                new Object[]{sysUser.getUsername()},
                new int[]{java.sql.Types.VARCHAR},
                SysUser.class);
    }

    //通过RowCallbackHandler对Select语句得到的每行记录进行解析，并为其创建一个User数据对象。实现了手动的OR映射。
    public SysUser queryUserById4(String id) {
        final SysUser sysUser = new SysUser();

        //该方法返回值为void
        this.jdbcTemplate.query("select * from tb_user where id = ?",
                new Object[]{id},
                new RowCallbackHandler() {

                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        SysUser sysUser = new SysUser();
                        sysUser.setId(rs.getLong("id"));
                        sysUser.setUsername(rs.getString("username"));
                        sysUser.setPassword(rs.getString("password"));
                    }
                });

        return sysUser;
    }

    @SuppressWarnings("unchecked")
    public List<SysUser> list(SysUser sysUser) {
        return jdbcTemplate.query("select * from tb_user where username like '%?%'",
                new Object[]{sysUser.getUsername()},
                new int[]{java.sql.Types.VARCHAR},
                new RowMapper() {

                    @Override
                    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                        SysUser sysUser = new SysUser();
                        sysUser.setId(rs.getLong("id"));
                        sysUser.setUsername(rs.getString("username"));
                        sysUser.setPassword(rs.getString("password"));
                        return sysUser;
                    }
                });
    }

    //批量操作    适合于增、删、改操作
    public int[] batchUpdate(final List users) {

        int[] updateCounts = jdbcTemplate.batchUpdate(
                "update tb_user set username = ?, password = ? where id = ?",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, ((SysUser) users.get(i)).getUsername());
                        ps.setString(2, ((SysUser) users.get(i)).getPassword());
                        ps.setLong(3, ((SysUser) users.get(i)).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return users.size();
                    }
                }
        );

        return updateCounts;
    }

    //调用存储过程
    public void callProcedure(int id) {
        this.jdbcTemplate.update("call `SUPPORT`.REFRESH_USERS_SUMMARY(?)", new Object[]{Long.valueOf(id)});

    }

}
