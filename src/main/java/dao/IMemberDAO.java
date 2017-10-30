package dao;

import vo.Member;

public interface IMemberDAO extends IDAO<String, Member> {
    boolean add(Member vo,String[] rid)throws Exception;

}
