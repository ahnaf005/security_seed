/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author seed
 */
public class Httpthread {

    private int success;
    private String _elggtoken;
    private String _elggts;
    private String cookie;
    private BufferedReader br = null;
    private HttpGet request;
    private String entity_string;
    private HttpPost httppost;
    private String username;
    private String password;
    public Httpthread() {
        success = 0;
    }
    public Httpthread(String username,String password) {
        this.username=username;
        this.password=password;
        success = 0;
    }
    public void httpclient() throws IOException {
        try {
            HttpClient client = HttpClientBuilder.create().build();
            request = new HttpGet("http://www.xsslabelgg.com");
            HttpResponse response = client.execute(request);

            //get all headers		
            Header[] headers = response.getAllHeaders();
            /*for (Header header : headers) {
                System.out.println("Key : " + header.getName()
                        + " ,Value : " + header.getValue());
            }*/
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            //System.out.println(content);
            cookie = response.getFirstHeader("Set-Cookie").getValue();
            int tmp_first = 0;
            int tmp_last = 0;
            tmp_first = cookie.indexOf('=');
            tmp_last = cookie.indexOf(';');
            cookie = cookie.substring(tmp_first + 1, tmp_last);
            //System.out.println("Cookie:" + cookie);
            tmp_first = content.indexOf("\"__elgg_ts\":");
            tmp_last = content.indexOf(",\"__elgg_token\"");
            _elggts = content.substring(tmp_first + "\"__elgg_ts\":".length(), tmp_last);
            //System.out.println("_elggts:" + _elggts);
            tmp_first = tmp_last;
            tmp_last = content.indexOf("\"}},\"session\"");
            _elggtoken = content.substring(tmp_first + 2 + ",\"__elgg_token\"".length(), tmp_last);
            //System.out.println("_elggtoken:" + _elggtoken);
            HttpClient httpclient = HttpClients.createDefault();
            httppost = new HttpPost("http://www.xsslabelgg.com/action/login");

// Request parameters and other properties.
            httppost.addHeader("Cookie", "Elgg=" + cookie);
            httppost.addHeader("Referer", "http://www.xsslabelgg.com/");
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("__elgg_token", _elggtoken));
            params.add(new BasicNameValuePair("__elgg_ts", _elggts));
            params.add(new BasicNameValuePair("username",username));
            params.add(new BasicNameValuePair("password", password));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

//Execute and get the response.
            success = 0;
            response = httpclient.execute(httppost);
            headers = response.getAllHeaders();
            for (Header header : headers) {
                //System.out.println("Key : " + header.getName()
                      //  + " ,Value : " + header.getValue());
                if (header.getName().equals("Set-Cookie")) {
                    success = 1;
                }
            }
            if (success == 1) {
                cookie = response.getFirstHeader("Set-Cookie").getValue();
                tmp_first = 0;
                tmp_last = 0;
                tmp_first = cookie.indexOf('=');
                tmp_last = cookie.indexOf(';');
                cookie = cookie.substring(tmp_first + 1, tmp_last);
                //System.out.println("Cookie:" + cookie);
                continue_further();
            }
            entity = response.getEntity();
            content = EntityUtils.toString(entity);
            //System.out.println(content);
        } finally {

            if (request != null) {

                request.releaseConnection();
            }
            if (httppost != null) {

                httppost.releaseConnection();
            }
        }

        //get header by 'key'
        //String server = response.getFirstHeader("Server").getValue();
    }

    public void continue_further() throws IOException {
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost("http://www.xsslabelgg.com/");

// Request parameters and other properties.
            httppost.addHeader("Cookie", "Elgg=" + cookie);
            httppost.addHeader("Referer", "http://www.xsslabelgg.com/");

//Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            Header[] headers = response.getAllHeaders();
            /*for (Header header : headers) {
                System.out.println("Key : " + header.getName()
                        + " ,Value : " + header.getValue());
            }*/
            httpclient = HttpClients.createDefault();
            httppost = new HttpPost("http://www.xsslabelgg.com/activity");

// Request parameters and other properties.
            httppost.addHeader("Cookie", "Elgg=" + cookie);
            httppost.addHeader("Referer", "http://www.xsslabelgg.com/");

//Execute and get the response.
            response = httpclient.execute(httppost);
            headers = response.getAllHeaders();
            /*for (Header header : headers) {
                System.out.println("Key : " + header.getName()
                        + " ,Value : " + header.getValue());

            }*/
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            entity_string = content;
            //System.out.println(content);
        } finally {
            if (httppost != null) {
                httppost.releaseConnection();
            }
        }
    }

    public int get_success() {
        return success;
    }

    public String get_entity() {
        return entity_string;
    }
//    public void get_request() throws MalformedURLException, IOException {
//        try {
//            URL obj = new URL("http://www.xsslabelgg.com");
//            URLConnection conn = obj.openConnection();
//            conn.setRequestProperty("User-Agent",
//                    "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:60.0) Gecko/20100101 Firefox/60.0");
//            //get all headers
//            conn.connect();
//            Map<String, List<String>> map = conn.getHeaderFields();
//            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
//                System.out.println("Key : " + entry.getKey()
//                        + " ,Value : " + entry.getValue());
//            }
//            List<String> cookie_list = map.get("Set-Cookie");
//
////        for(String string:cookie_list)
////        {
////            System.out.println(string);
////        }
//            int tmp_first = cookie_list.get(0).indexOf('=');
//            int tmp_last = cookie_list.get(0).indexOf(';');
//            cookie = cookie_list.get(0).substring(tmp_first + 1, tmp_last);
//            System.out.println("cookie: " + cookie);
//            br = new BufferedReader(new InputStreamReader(obj.openStream()));
//
//            String line;
//
//            StringBuilder sb = new StringBuilder();
//
//            while ((line = br.readLine()) != null) {
//
//                sb.append(line);
//                sb.append(System.lineSeparator());
//            }
//
//            //System.out.println(sb);
//            tmp_first = sb.indexOf("\"__elgg_ts\":");
//            tmp_last = sb.indexOf(",\"__elgg_token\"");
//            _elggts = sb.substring(tmp_first + "\"__elgg_ts\":".length(), tmp_last);
//            System.out.println("_elggts:" + _elggts);
//            tmp_first = tmp_last;
//            tmp_last = sb.indexOf("\"}},\"session\"");
//            _elggtoken = sb.substring(tmp_first + 2 + ",\"__elgg_token\"".length(), tmp_last);
//            System.out.println("_elggtoken:" + _elggtoken);
//            //get all headers
//            map = conn.getHeaderFields();
//            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
//                System.out.println("Key : " + entry.getKey()
//                        + " ,Value : " + entry.getValue());
//            }
//        } finally {
//            if (br != null) {
//                br.close();
//            }
//        }
//    }
//
//    public void post_request() throws MalformedURLException, IOException {
//        try {
//            String urlParameters = "__elgg_token=" + _elggtoken + "&__elgg_ts=" + _elggts + "&username=boby&password=seedboby";
//            System.out.println(urlParameters);
//            byte[] postData = urlParameters.getBytes();
//            int postDataLength = urlParameters.length();
//            String request = "http://www.xsslabelgg.com/action/login";
//            URL url = new URL(request);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setDoOutput(true);
//            conn.setInstanceFollowRedirects(false);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Host", "www.xsslabelgg.com");
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            //conn.setRequestProperty("charset", "utf-8");
//            conn.setRequestProperty("Cookie", "Elgg=" + cookie);
//            conn.setRequestProperty("Referer", "http://www.xsslabelgg.com/");
//            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
//            conn.setUseCaches(false);
//            conn.setRequestProperty("User-Agent",
//                    "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:60.0) Gecko/20100101 Firefox/60.0");
//            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//            conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
//            conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
//            System.out.println(conn.getRequestProperties().size());
//            for (String header : conn.getRequestProperties().keySet()) {
//                if (header != null) {
//                    for (String value : conn.getRequestProperties().get(header)) {
//                        System.out.println(header + ":" + value);
//                    }
//                }
//            }
//            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
//                wr.write(postData);
//            }
//            System.out.println("Response code:" + conn.getResponseCode());
//            Map<String, List<String>> map = conn.getHeaderFields();
//            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
//                System.out.println("Key : " + entry.getKey()
//                        + " ,Value : " + entry.getValue());
//            }
//            br = new BufferedReader(new InputStreamReader(url.openStream()));
//
//            String line;
//
//            StringBuilder sb = new StringBuilder();
//
//            while ((line = br.readLine()) != null) {
//
//                sb.append(line);
//                sb.append(System.lineSeparator());
//            }
//            //System.out.println(sb);
//        } finally {
//            if (br != null) {
//                br.close();
//            }
//        }
//    }
}
