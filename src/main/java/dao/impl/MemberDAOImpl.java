package dao.impl;

import dao.AbstractDAOImpl;
import dao.IMemberDAO;
import vo.Member;

public class MemberDAOImpl extends AbstractDAOImpl<String, Member> implements IMemberDAO {
    public MemberDAOImpl() throws Exception {
        super(Member.class);
    }
}
