package dao.impl;

import dao.AbstractDAOImpl;
import dao.IMemberDAO;
import vo.Member;

public class MemberDAOImpl extends AbstractDAOImpl<String, Member> implements IMemberDAO {
    public MemberDAOImpl() throws Exception {
        super(Member.class);
    }

    @Override
    public boolean add(Member vo, String[] rid) throws Exception {
        if (doCreate(vo)) {
            sql = "insert into member_role(mid,rid)values(?,?)";
            pre = conn.prepareStatement(sql);
            for (String x : rid) {
                pre.setString(1, vo.getMid());
                pre.setInt(2, Integer.valueOf(x));
                pre.addBatch();
            }
            int[] count = pre.executeBatch();
            return count.length == rid.length;
        }
        return false;
    }
}
