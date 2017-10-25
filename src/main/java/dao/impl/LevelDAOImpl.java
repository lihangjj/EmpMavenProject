package dao.impl;

import dao.AbstractDAOImpl;
import dao.ILevelDAO;
import vo.Level;

public class LevelDAOImpl extends AbstractDAOImpl<Integer, Level> implements ILevelDAO {
    public LevelDAOImpl() throws Exception {
        super(Level.class);
    }
}
