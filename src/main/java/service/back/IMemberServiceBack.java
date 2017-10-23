package service.back;

import vo.Member;

import java.util.Map;

public interface IMemberServiceBack {
    /**
     *
     * @param vo
     * @return 要返回的内容
     * key=flag,value=true|false,表示登陆成功或失败的标记
     * key=sflag,value=0|1,是否为超级管理员
     * key=name,value=真实姓名，当前用户的真实姓名，此姓名要在前台显示
     * key=allRoles,value=Set,所有角色数据
     * key=allActions,value=Set,所有的权限数据
     * @throws Exception
     */
    Map<String, Object> login(Member vo) throws Exception;
}
