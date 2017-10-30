package util.servlet;


import com.jspsmart.upload.SmartUpload;
import com.jspsmart.upload.SmartUploadException;
import util.DateUtil;
import util.validate.ValidateParameter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.*;

import static dao.AbstractDAOImpl.initCap;

public abstract class DispatcherServlet extends HttpServlet {

    protected SmartUpload smart = null;
    protected String forwardPath = "forward.page";
    protected String referer = "";
    protected String title = "";
    private static final String PAGES_BASENAME = "Pages";
    private static final String MESSAGES_BASENAME = "Messages";
    private ResourceBundle pagesResource;
    private ResourceBundle messagesResource;
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    protected Integer currentPage = 1;
    protected Integer lineSize = 5;
    protected String keyWord = "";
    protected String column = "";
    protected String columnData = null;
    protected Class lastClass = this.getClass();
    protected Object lastObject = this;
    protected Method lastSetMethod;
    protected Method lastGetMethod;

    public void init() throws ServletException {

        this.pagesResource = ResourceBundle.getBundle(PAGES_BASENAME,
                Locale.getDefault());
        this.messagesResource = ResourceBundle.getBundle(MESSAGES_BASENAME,
                Locale.getDefault());
    }

    ;

    public void handleSplit(String url) {
        String cp = request.getParameter("currentPage");
        String ls = request.getParameter("lineSize");
        String kw = request.getParameter("keyWord");
        String co = request.getParameter("column");
        this.currentPage = cp == null || "".equals(cp) ? currentPage : Integer.parseInt(cp);
        this.lineSize = ls == null || "".equals(ls) ? lineSize : Integer.parseInt(ls);
        this.keyWord = kw == null || "".equals(kw) ? "" : kw;
        this.column = co == null || "".equals(co) ? column : co;
        this.request.setAttribute("currentPage", this.currentPage);
        this.request.setAttribute("lineSize", this.lineSize);
        this.request.setAttribute("column", this.column);
        this.request.setAttribute("keyWord", this.keyWord);
        this.request.setAttribute("url", getPath(url));
        this.request.setAttribute("columnData", columnData);

    }


    /**
     * 取得在Pages.properties文件里面定义的访问路径
     *
     * @param key 访问路径的key
     * @return 配置文件中的路径内容，如果没有返回null
     */
    public String getPath(String key) {
        return this.pagesResource.getString(key);
    }

    /**
     * 取得Messages.properties文件中的配置文字信息
     *
     * @param key  访问文字信息的key
     * @param args 所有占位符的内容
     * @return 配置文件中的内容，并且是组合后的结果，如果没有返回null
     */
    public String getMsg(String key, String... args) {
        String note = this.messagesResource.getString(key);
        if (args.length > 0 && title != null) { // 传递了参数内容
            return MessageFormat.format(note, args);
        } else {
            return MessageFormat.format(note, "");
        }
    }

    public HttpSession getSession() {
        return request.getSession();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        this.request = request;
        this.response = response;
        response.setCharacterEncoding("utf-8");
        String path = this.getPath("errors.page");
        String status = getStatus();
        if (request.getContentType() != null) {//先要判断不为空，否则要出现空指针异常
            if (request.getContentType().contains("multipart/form-data")) {//看看是不是表单提交
                this.smart = new SmartUpload();
                try {
                    this.smart.initialize(super.getServletConfig(), request,
                            response);
                    this.smart.upload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                smart = null;
            }
        } else {
            smart = null;
        }
        // 现在可以找到当前类对象this，以及要调用的方法名称status，那么可以利用反射进行调用
        if (status != null && status.length() > 0) {
            // 在进行参数的处理之前，需要对提交数据进行验证
            Map<String, String> errors = ValidateParameter.validate(this);
            if (errors.size() == 0) { // 没有错误
                try { // 只有将对应的数据都准备完毕了，才可以执行以下方法
                    Method method = this.getClass().getDeclaredMethod(status);
                    method.setAccessible(true);//取消方法封装
                    if (method.getGenericReturnType().toString().equals("void")) {//判断方法返回值是否为void,如果是代表ajax
                        // 异步数据交互，不必自动赋值Vo，执行方法，然后结束
                        method.invoke(this);
                        return;
                    } else {
                        autoSetVo();
                        path = this.getPath((String) method.invoke(this));// 反射调用方法
                        clearVoValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else { // 回到指定的错误页上
                request.setAttribute("errors", errors);
            }
        }
        request.getRequestDispatcher(path).forward(request, response);
    }

    public void autoSetVo() throws Exception {
        Enumeration<String> enumeration = smart == null ? request.getParameterNames() : smart.getRequest().getParameterNames();
        //得到当前类的Class对象
        lastClass = this.getClass();
        lastObject = this;
        Field lastField = null;
        while (enumeration.hasMoreElements()) {
            String parameterName = enumeration.nextElement();
            if (parameterName.contains(".")) {//如果包含.说明需要自动赋值
                String[] nameValue = parameterName.split("\\.");
                for (int x = 0; x < nameValue.length - 1; x++) {
                    lastGetMethod = lastClass.getDeclaredMethod("get" + initCap(nameValue[x]));
                    lastObject = lastGetMethod.invoke(lastObject);
                    lastClass = lastObject.getClass();
                    lastSetMethod = lastClass.getDeclaredMethod("set" + initCap(nameValue[x + 1]), lastClass.getDeclaredField(nameValue[x + 1]).getType());
                    lastField = lastClass.getDeclaredField(nameValue[x + 1]);
                }
                String value = smart == null ? request.getParameter(parameterName) : smart.getRequest().getParameter(parameterName);
                switch (lastField.getType().getSimpleName()) {//根据成员类型对VO的成员设置值,这里因为有参数，所以value一定不为null
                    case "String":
                        lastSetMethod.invoke(lastObject, value);
                        break;
                    case "Integer":
                        if (!"".equals(value)) {
                            lastSetMethod.invoke(lastObject, Integer.valueOf(value));
                        }
                        break;
                    case "Date":
                        if (!"".equals(value)) {

                            lastSetMethod.invoke(lastObject, DateUtil.getEglishUtilDate(value));//将英文日期格式转换成util.Date
                        }
                        break;
                    case "Double":
                        if (!"".equals(value)) {
                            lastSetMethod.invoke(lastObject, Double.valueOf(value));
                        }
                        break;
                }
                //一次赋值之后，需要将当前CLass对象还原成this
                lastClass = this.getClass();
                lastObject = this;
            }
        }
        System.out.println(lastClass.getDeclaredFields()[0].get(this));
    }

    public void clearVoValue() throws Exception {
        Object oldVo = this.getClass().getDeclaredFields()[0].get(this);
        Field allVoField[] = oldVo.getClass().getDeclaredFields();
        Integer i = null;
        for (Field x : allVoField) {
            oldVo.getClass().getMethod("set" + initCap(x.getName()), x.getType()).invoke(oldVo, i);
        }


    }

    /**
     * /**
     * 此方法主要是判断是否有文件上传
     *
     * @return 没有文件上传返回false
     */
    public boolean isUpload() {
        try {
            return this.smart.getFiles().getSize() > 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getStringParameter(String paramName) {
        String contentType = request.getContentType();
        if (contentType != null && contentType.contains("multipart/form-data")) {
            return this.smart.getRequest().getParameter(paramName);
        } else {
            return this.request.getParameter(paramName);
        }
    }

    /**
     * 取得上传文件的个数
     *
     * @return
     */
    public int getUploadCount() {
        return this.smart.getFiles().getCount();
    }

    /**
     * 专门负责文件的保存操作
     *
     * @param index    SmartUpload操作索引
     * @param fileName 文件名称
     * @throws SmartUploadException
     * @throws IOException
     */
    private void saveFile(int index, String fileName) throws IOException,
            SmartUploadException {
        String filePath = super.getServletContext().getRealPath(
                this.getUploadDirectory())
                + fileName;
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (this.smart.getFiles().getFile(index).getContentType()
                .contains("image")) {
            this.smart.getFiles().getFile(index).saveAs(filePath);
        }
    }

    /**
     * 删除文件
     *
     * @param fileName
     */
    public void deleteFile(String fileName) {
        String filePath = super.getServletContext().getRealPath(
                this.getUploadDirectory())
                + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 批量删除上传文件
     *
     * @param all
     */
    public void deleteFile(Set<String> all) {
        Iterator<String> iter = all.iterator();
        while (iter.hasNext()) {
            this.deleteFile(iter.next());
        }
    }

    /**
     * 存放一个文件信息
     *
     * @param fileName
     */
    public void save(String fileName) {
        try {
            this.saveFile(0, fileName);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SmartUploadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void save(Map<Integer, String> fileName) {
        Iterator<Map.Entry<Integer, String>> iter = fileName.entrySet()
                .iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, String> me = iter.next();
            try {
                this.saveFile(me.getKey(), me.getValue());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SmartUploadException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建一个新的文件名称
     *
     * @return
     */
    public String createSingleFileName() {
        String fileName = "nophoto.png";
        if (this.isUpload()) {
            if (this.smart.getFiles().getFile(0).getContentType()
                    .contains("image")) {
                fileName = UUID.randomUUID() + "."
                        + this.smart.getFiles().getFile(0).getFileExt();
            }
        }
        return fileName;
    }

    public Map<Integer, String> createMultiFileName() {
        Map<Integer, String> all = new HashMap<>();
        if (this.isUpload()) {
            for (int x = 0; x < this.smart.getFiles().getCount(); x++) {
                if (this.smart.getFiles().getFile(x).getContentType()
                        .contains("image")) {
                    String fileName = UUID.randomUUID() + "."
                            + this.smart.getFiles().getFile(x).getFileExt();
                    all.put(x, fileName);
                }
            }
        }
        return all;
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.doGet(request, response);
    }


    public void setMsgAndUrl(String msgKey, String urlKey) {
        this.request.setAttribute("msg", this.getMsg(msgKey, title));
        this.request.setAttribute("url", this.getPath(urlKey));
    }

    public void setMsgAndBack(String msgKey) {
        this.request.setAttribute("msg", this.getMsg(msgKey, title));
        this.request.setAttribute("url", referer);
    }

    /**
     * 交由不同的子类来实现，可以由子类设置统一的占位符提示信息名称标记
     *
     * @return 返回不同子类的描述信息
     */

    /**
     * 取得要进行文件上传保存的目录
     *
     * @return
     */
    public abstract String getUploadDirectory();


    public Map<Integer, Object> autoSetSameVo() throws Exception {
        Enumeration<String> enumeration = smart == null ? request.getParameterNames() : smart.getRequest().getParameterNames();
        //得到当前类的Class对象
        Map<Integer, Object> allFirstVo = new HashMap<>();
        Map<Integer, Object> allLastVo = new HashMap<>();
        Map<String, Object> isExistVo = new HashMap<>();
        Class lastClass = this.getClass();
        Object lastObject = this;
        boolean flag = true;
        while (enumeration.hasMoreElements()) {
            String parameterName = enumeration.nextElement();
            if (parameterName.contains(".")) {//如果包含.说明需要自动赋值
                String[] nameValue = parameterName.split("\\.");
                if (nameValue.length == 2) {
                    for (int x = 0; x < nameValue.length - 1; x++) {
                        //利用当前类的Class对象取得getVo方法
                        Method getVoMethod = lastClass.getDeclaredMethod("get" + initCap(nameValue[x]));
                        //用get方法获取已经实例化了的成员对象（vo类）,但是如果当前vo类已经赋值过，那就需要清空vo的值
                        Object o = getVoMethod.invoke(lastObject);
                        lastObject = o;//改变当前对象为vo类
                        //获得vo类对象的Class对象,并改变当前Class对象为VO的Class
                        lastClass = o.getClass();
                    }
                    //利用当前Class对象来获取vo成员
                    Field field = lastClass.getDeclaredField(nameValue[1]);
                    //利用vo类对象的Class对象来获取vo中的set方法,参数名，和参数类型
                    Method setMethod = lastClass.getDeclaredMethod("set" + initCap(nameValue[1]), field.getType());
                    //利用vo中的set方法给vo设置值
                    String values[] = smart == null ? request.getParameterValues(parameterName) : smart.getRequest().getParameterValues(parameterName);

                    for (int x = 0; x < values.length; x++) {
                        //如果是第一次设置vo的值，那就实例化新的一个Vo类，如果是第二次，直接从set集合里边取出vo
                        Object vo;
                        if (flag) {
                            vo = lastClass.getConstructor().newInstance();
                        } else {
                            vo = allFirstVo.get(x);
                        }
                        switch (field.getType().getSimpleName()) {//根据成员类型对VO的成员设置值,这里因为有参数，所以value一定不为null
                            case "String":
                                setMethod.invoke(vo, values[x]);
                                break;
                            case "Integer":
                                if (!"".equals(values[x])) {
                                    setMethod.invoke(vo, Integer.valueOf(values[x]));
                                }
                                break;
                            case "Date":
                                if (!"".equals(values[x])) {
                                    setMethod.invoke(vo, Date.valueOf(values[x]));
                                }
                                break;
                            case "Double":
                                if (!"".equals(values[x])) {
                                    setMethod.invoke(vo, Double.valueOf(values[x]));
                                }
                                break;
                        }
                        //将设置好值的第一个VO类对象加入到集合中
                        allFirstVo.put(x, vo);
                    }
                    flag = false;//表示接下来赋值下一个vo的成员
                } else {//如果是三级以上操作
                    //用来保存所有的每级vo对象
                    Map<Integer, Object> objectMap = new HashMap<>();
                    //用来保存每级vo的set方法
                    Map<Integer, Method> setMethodMap = new HashMap<>();
                    //用来保存每集vo的get方法
                    Map<Integer, Method> getMethodMap = new HashMap<>();
                    for (int x = 0; x < nameValue.length - 1; x++) {
                        //利用当前类的Class对象取得getVo方法
//                        Method getVo1 = lastClass.getDeclaredMethod("get" + initCap(nameValue[x]));
//                        Object vo1 = getVo1.invoke(lastObject);
//                        Method setVo1 = vo1.getClass().getDeclaredMethod("set" + initCap(nameValue[x + 1]), vo1.getClass().getDeclaredField(nameValue[x + 1]).getType());
//
//                        lastClass = vo1.getClass();
//                        lastObject = vo1;
//
//                        Method getVo2 = vo1.getClass().getDeclaredMethod("get" + initCap(nameValue[x + 1]));
//                        Object vo2 = getVo2.invoke(vo1);
//                        Method setVo2 = vo2.getClass().getDeclaredMethod("set" + initCap(nameValue[x + 2]), vo2.getClass().getDeclaredField(nameValue[x + 2]).getType());
//
//                        setVo1.invoke(vo1, vo2);
//                        setVo2.invoke(vo2, 2);

                        Method getVo = lastClass.getDeclaredMethod("get" + initCap(nameValue[x]));
                        Object vo = getVo.invoke(lastObject);
                        Method setVo = vo.getClass().getDeclaredMethod("set" + initCap(nameValue[x + 1]), vo.getClass().getDeclaredField(nameValue[x + 1]).getType());
                        objectMap.put(x, vo);//0，就是第一级vo，如emp
                        setMethodMap.put(x, setVo);//0，就是emp.setDept()
                        getMethodMap.put(x, getVo);//0,就是getEmp()
                        lastClass = vo.getClass();
                        lastObject = vo;
                    }
                    //利用vo中的set方法给vo设置值
                    String values[] = smart == null ? request.getParameterValues(parameterName) : smart.getRequest().getParameterValues(parameterName);
                    for (int x = 0; x < values.length; x++) {
                        //如果是第一次设置vo的值，那就实例化新的一个Vo类，如果是第二次，直接从vo1map集合里边取出vo
                        Object firstVo;
                        Object lastVo;//最后一级vo的设置
                        if (flag) {
                            Object vo1 = objectMap.get(0);//
                            firstVo = vo1.getClass().getConstructor().newInstance();//实例化一个新的一级vo
                            //递归调用，得到最后一级vo
                        } else {
                            firstVo = allFirstVo.get(x);
                        }
                        if (!isExistVo.containsKey(getMethodMap.get(nameValue.length - 2).getName())) {
                            //如果不包含取得最后vo类名字的键值，说明还没有得到过最后的vo,需要得到一次
                            lastVo = getLastObj(nameValue.length - 2, firstVo, getMethodMap);
                        } else {
                            //如果已经存了，直接取
                            lastVo = allLastVo.get(x);
                        }

                        switch (objectMap.get(nameValue.length - 2).getClass().getDeclaredField(nameValue[nameValue.length - 1]).getType().getSimpleName()) {//根据最后一集成员类型对VO的成员设置值
                            case "String":
                                setMethodMap.get(nameValue.length - 2).invoke(lastVo, values[x]);
                                break;
                            case "Integer":
                                if (!"".equals(values[x])) {
                                    setMethodMap.get(nameValue.length - 2).invoke(lastVo, Integer.valueOf(values[x]));
                                }
                                break;
                            case "Date":
                                if (!"".equals(values[x])) {
                                    setMethodMap.get(nameValue.length - 2).invoke(lastVo, Date.valueOf(values[x]));
                                }
                                break;
                            case "Double":
                                if (!"".equals(values[x])) {
                                    setMethodMap.get(nameValue.length - 2).invoke(lastVo, Double.valueOf(values[x]));
                                }
                                break;
                            default:

                        }
                        //将设置好值的VO类对象加入到集合中
                        allFirstVo.put(x, firstVo);//专门用来保存所有的一集Vo类
                        allLastVo.put(x, lastVo);
                    }
                    flag = false;
                    //保存lastVO已经取得过的标记，而后再遇到就不是创建，而是从allLastMap中取得
                    isExistVo.put(getMethodMap.get(nameValue.length - 2).getName(), "存在");

                }
                //一次赋值之后，需要将当前CLass对象还原成this
                lastClass = this.getClass();
                lastObject = this;
            }

        }
        return allFirstVo;
    }

    /**
     * 取得当前的业务调用的参数
     *
     * @return
     */
    public String getStatus() {
        String uri = request.getRequestURI(); // 取得URI，内容：/DispatcherProject/EmpServlet/edit
        String status = uri.substring(uri.lastIndexOf("/") + 1);
        return status;
    }

    public int length = 0;

    /**
     * 递归得到最后一级vo
     *
     * @param length1      循环条件
     * @param first        第一个vo
     * @param getMethodMap getVo的map集合
     * @return
     * @throws Exception
     */
    public Object getLastObj(int length1, Object first, Map<Integer, Method> getMethodMap) throws Exception {
        Object obj = getMethodMap.get(length + 1).invoke(first);
        length++;
        if (length < length1) {
            return getLastObj(length1, obj, getMethodMap);
        }
        length = 0;
        return obj;
    }

}
