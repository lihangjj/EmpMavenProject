package dao.impl;

import dao.AbstractDAOImpl;
import dao.IElogDAO;
import vo.Elog;

public class ElogDAOImpl extends AbstractDAOImpl<Integer, Elog> implements IElogDAO {
    public ElogDAOImpl() throws Exception {
        super(Elog.class);
    }
}
