package dao;



import dbc.DatabaseConnection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.Date;

public abstract class AbstractDAOImpl<K, V> implements IDAO<K, V> {
    protected Connection conn;     // protected 修饰所有本类成员目的是方便子类使用
    protected PreparedStatement pre;
    protected ResultSet res;
    protected String sql;
    protected StringBuffer buf = new StringBuffer();
    protected String tableName;//表名
    protected Class cls;  //class类对象
    protected List<Field> fields = new ArrayList<>();//VO类的成员集合

    public AbstractDAOImpl(Class v) throws Exception {//其实这里应该用泛型表示<T> T
//        this.conn = conn;
        this.conn = DatabaseConnection.get();
        cls = v;//VO的Class对象
        tableName = cls.getSimpleName().toLowerCase();//将Vo类名变为小写后，就是表名
        sql = "desc " + tableName;//查询表结构，结果是一张表，第一列是表字段
        pre = conn.prepareStatement(sql);
        res = pre.executeQuery();
        while (res.next()) {
            fields.add(cls.getDeclaredField(res.getString(1)));//第一列就是表字段，通过该字段找到VO类的成员，
            // 将需要的VO成员添加到List集合，以便其他方法使用。
        }
    }
    {

    }

    public Set<String> photoHandle(String table, String photoColumn,
                                   String column, Set<?> ids) throws SQLException {
        Set<String> all = new HashSet<String>();
        buf.setLength(0);
        buf.append("SELECT ").append(photoColumn).append(" FROM ")
                .append(table).append(" WHERE ").append(column).append(" IN (");
        Iterator<?> iter = ids.iterator();
        while (iter.hasNext()) {
            buf.append(iter.next()).append(",");
        }
        buf.delete(buf.length() - 1, buf.length()).append(")");
        buf.append(" AND ").append(photoColumn).append("<>'nophoto.jpg'");
        this.pre = this.conn.prepareStatement(buf.toString());
        ResultSet rs = this.pre.executeQuery();
        while (rs.next()) {
            all.add(rs.getString(1));
        }
        return all;
    }

    public boolean doCreate(V vo) throws Exception {
        buf.setLength(0);
        buf.append("insert into " + tableName + "(");
        for (Field x : fields) {
            buf.append(x.getName()).append(",");
        }
        buf.delete(buf.length() - 1, buf.length()).append(")values(");
        //有几个字段需要设置，就有y个问号
        for (int x = 0; x < fields.size(); x++) {
            buf.append("?,");
        }
        buf.delete(buf.length() - 1, buf.length()).append(")");
        pre = conn.prepareStatement(buf.toString());
        voGet(vo);
        return pre.executeUpdate() > 0;
    }

    private void voGet(V vo) throws Exception {//相当于vo.getXxx()方法，取出vo的值，然后保存到数据库去
        for (int x = 0; x < fields.size(); x++) {
            Method getMethod = cls.getMethod("get" + initCap(fields.get(x).getName()));
            switch (fields.get(x).getType().getSimpleName()) {
                case "String":
                    pre.setString(x + 1, (String) getMethod.invoke(vo));
                    break;
                case "Integer":
                    if (getMethod.invoke(vo) == null) {
                        pre.setInt(x + 1, Types.NULL);
                    } else {
                        pre.setInt(x + 1, (int) getMethod.invoke(vo));
                    }
                    break;
                case "Double":
                    if (getMethod.invoke(vo) == null) {
                        pre.setDouble(x + 1, Types.NULL);
                    } else {
                        pre.setDouble(x + 1, (Double) getMethod.invoke(vo));
                    }
                    break;
                case "Date":
                    pre.setTimestamp(x + 1, getMethod.invoke(vo) == null ? null : new Timestamp(((Date) getMethod.invoke(vo)).getTime()));
                    break;

            }
        }
    }

    /**
     * 首字母大写的方法，比较高大上的办法
     * @param name
     * @return
     */
    public static String initCap(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }

    /**
     * 查询全部数据
     * @return
     * @throws Exception
     */
    @Override
    public List<V> findAll() throws Exception {
        List<V> list = new ArrayList<>();
        bufSelectAll();
        pre = conn.prepareStatement(buf.toString());
        res = pre.executeQuery();
        while (res.next()) {
            V vo = (V) cls.newInstance();
            voSet(vo);
            list.add(vo);
        }
        return list;
    }

    protected void voSet(V vo) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, SQLException {
        for (int x = 0; x < fields.size(); x++) {//相当于vo.setXxx()方法给VO赋值
            Method setMethod = cls.getMethod("set" + initCap(fields.get(x).getName()), fields.get(x).getType());
            switch (fields.get(x).getType().getSimpleName()) {
                case "String":
                    setMethod.invoke(vo, res.getString(x + 1));
                    break;
                case "Integer":
                    setMethod.invoke(vo, res.getInt(x + 1));
                    break;
                case "Date":
                    setMethod.invoke(vo, res.getDate(x + 1));
                    break;
                case "Double":
                    setMethod.invoke(vo, res.getDouble(x + 1));
                    break;

            }
        }
    }

    /**
     * 数据更新操作
     * @param vo 包含了要修改数据的信息，一定要提供有ID内容
     * @return
     * @throws Exception
     */
    @Override
    public boolean doUpdate(V vo) throws Exception {
        buf.setLength(0);
        buf.append("update " + tableName + " set ");
        for (Field x : fields) {
            buf.append(x.getName() + "=?,");
        }
        buf.delete(buf.length() - 1, buf.length()).append(" where " + fields.get(0).getName() + "=?");
        pre = conn.prepareStatement(buf.toString());
        voGet(vo);
        Method getMethod = cls.getMethod("get" + initCap(fields.get(0).getName()));
        switch (fields.get(0).getType().getSimpleName()) {
            case "String":
                pre.setString(fields.size() + 1, (String) getMethod.invoke(vo));
                break;
            case "Integer":
                if (getMethod.invoke(vo) == null) {
                    pre.setInt(fields.size() + 1, Types.NULL);
                } else {
                    pre.setInt(fields.size() + 1, (int) getMethod.invoke(vo));
                }
                break;
        }
        return pre.executeUpdate() > 0;
    }

    /**
     * @param ids 包含了所有要删除的数据ID，不包含有重复内容
     * @return
     * @throws Exception
     */
    @Override
    public boolean doRemoveBatch(Set<K> ids) throws Exception {
        buf.setLength(0);
        buf.append("delete from " + tableName + " where " + fields.get(0).getName() + " in(");
        Iterator<K> iterator = ids.iterator();
        while (iterator.hasNext()) {
            K key = iterator.next();
            buf.append(key + ",");
        }
        buf.delete(buf.length() - 1, buf.length()).append(")");
        pre = conn.prepareStatement(buf.toString());
        return pre.executeUpdate() == ids.size();
    }

    /**
     *
     * @param id 要查询的对象编号
     * @return
     * @throws Exception
     */
    @Override
    public V findById(K id) throws Exception {
        bufSelectAll();
        buf.append(" where " + fields.get(0).getName() + "=?");
        pre = conn.prepareStatement(buf.toString());
        switch (id.getClass().getSimpleName()) {
            case "String":
                pre.setString(1, (String) id);
                break;
            case "Integer":
                pre.setInt(1, (Integer) id);
                break;
        }
        res = pre.executeQuery();
        V vo = null;
        while (res.next()) {
            vo = (V) cls.newInstance();
            voSet(vo);
        }
        return vo;
    }

    /**
     * 拼接sql语句中的select部分
     */
    protected void bufSelectAll() {
        buf.setLength(0);//StringBuffer对象清空
        buf.append("select ");
        for (Field x : fields) {
            buf.append(x.getName()).append(",");
        }
        buf.delete(buf.length() - 1, buf.length()).append(" from " + tableName);
    }

    /**
     * 分页查询全部数据
     * @param currentPage 当前所在的页
     * @param lineSize    每有莪显示数据行数
     * @param column      要进行模糊查询的数据列
     * @param keyWord     模糊查询的关键字
     * @return
     * @throws Exception
     */
    @Override
    public List<V> findAllSplit(Integer currentPage, Integer lineSize, String column, String keyWord) throws Exception {
        List<V> list = new ArrayList<>();
        buf.setLength(0);
        bufSelectAll();
        buf.append(" where " + column + " like ? limit " + (currentPage - 1) * lineSize + "," + lineSize);
        pre = conn.prepareStatement(buf.toString());
        pre.setString(1, "%" + keyWord + "%");
        res = pre.executeQuery();
        while (res.next()) {
            V vo = (V) cls.newInstance();
            voSet(vo);
            list.add(vo);
        }
        return list;
    }

    /**
     * 得到全部分页数据总数
     * @param column  要进行模糊查询的数据列
     * @param keyWord 模糊查询的关键字
     * @return
     * @throws Exception
     */
    @Override
    public Integer getAllCount(String column, String keyWord) throws Exception {
        buf.setLength(0);
        buf.append("select count(*) from " + tableName + " where " + column + " like ?");
        pre = conn.prepareStatement(buf.toString());
        pre.setString(1, "%" + keyWord + "%");
        res = pre.executeQuery();
        if (res.next()) {
            return res.getInt(1);
        }
        return 0;
    }

    /**
     * 要制定状态的分页方法
     * @param status
     * @param currentPage
     * @param lineSize
     * @param column
     * @param keyWord
     * @return
     * @throws Exception
     */
    public List<V> findAllSplitByStatus(Integer status, Integer currentPage, Integer lineSize, String column, String keyWord) throws Exception {
        List<V> list = new ArrayList<>();
        buf.setLength(0);
        bufSelectAll();
        buf.append(" where status=" + status + " and " + column + " like ? limit " + (currentPage - 1) * lineSize + "," + lineSize);
        pre = conn.prepareStatement(buf.toString());
        pre.setString(1, "%" + keyWord + "%");
        res = pre.executeQuery();
        V vo;
        while (res.next()) {
            vo = (V) cls.newInstance();
            voSet(vo);
            list.add(vo);
        }
        return list;
    }

    /**
     * 获得全部数据条数，有状态要求的
     * @param status
     * @param column
     * @param keyWord
     * @return
     * @throws Exception
     */
    public Integer getAllCountByStatus(Integer status, String column, String keyWord) throws Exception {
        buf.setLength(0);
        buf.append("select count(*) from " + tableName + " where status=" + status + " and " + column + " like ?");
        pre = conn.prepareStatement(buf.toString());
        pre.setString(1, "%" + keyWord + "%");
        res = pre.executeQuery();
        while (res.next()) {
            return res.getInt(1);
        }
        return 0;
    }
}
