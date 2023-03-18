package com.tbc.ddd.common.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * web 工具 公共 帮助类
 */
@Slf4j
public class WebUtils {

    /**
     * 使用Nginx做分发处理时，获取客户端IP的方法
     *
     * @param request
     * @return
     */
    public static String getNgigxAddress(HttpServletRequest request) {
        String ip = request.getHeader("http_x_connecting_ip");
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取当前访问页面经response.encodeURL处理后的URL(绝对路径)
     *
     * @param request
     * @param response
     * @return
     */
    public static String getRewritedURL(HttpServletRequest request, HttpServletResponse response) {

        return new StringBuilder(request.getRequestURL().toString().replace(request.getRequestURI(), ""))
            .append(getRewritedURI(request, response)).toString();
    }

    /**
     * 获取当前访问页面经response.encodeURL处理后的URI(相对路径)
     *
     * @param request
     * @param response
     * @return
     */
    public static String getRewritedURI(HttpServletRequest request, HttpServletResponse response) {

        return response.encodeURL(getRequestURI(request).toString());

    }

    /**
     * 获取当前访问页面的URL(绝对路径)
     *
     * @param request
     * @return
     */
    public static String getRequestURL(HttpServletRequest request) {

        StringBuilder url = new StringBuilder(request.getRequestURL());

        String queryString = request.getQueryString();

        if (StringUtils.isNotBlank(queryString)) {
            url.append("?").append(queryString);
        }

        return url.toString();
    }

    /**
     * 获取当前访问页面的URI(相对路径)
     *
     * @param request
     * @return
     */
    public static String getRequestURI(HttpServletRequest request) {

        StringBuilder url = new StringBuilder(request.getRequestURI());

        String queryString = request.getQueryString();

        if (StringUtils.isNotBlank(queryString)) {
            url.append("?").append(queryString);
        }

        return url.toString();
    }

    /**
     * 获取request请求中的表单数据及参数
     *
     * @param request
     * @return
     */
    public static Map<String, String> getRequestParams(HttpServletRequest request) {
        Map<String, String[]> m = request.getParameterMap();
        Map<String, String> ret = new HashMap<>(10);
        Iterator<String> keys = m.keySet().iterator();

        while (keys.hasNext()) {
            String key = keys.next();
            ret.put(key, StringUtils.join(m.get(key), ","));
        }

        return ret;
    }

    /**
     * 根据所提供的URL获取网页内容
     *
     * @param url
     * @return
     */
    public static String getHtmlContent(String url) {
        return getHtmlContent(url, 10 * 1000, 15 * 1000, StandardCharsets.UTF_8);
    }

    /**
     * 根据所提供的URL获取网页内容
     *
     * @param url
     * @param charset
     * @return
     */
    public static String getHtmlContent(String url, Charset charset) {
        return getHtmlContent(url, 10 * 1000, 15 * 1000, charset);
    }

    /**
     * 根据所提供的URL获取网页内容
     *
     * @param url
     * @param charset
     * @param connectTimeout
     * @param readTimeout
     * @return
     */
    public static String getHtmlContent(String url, int connectTimeout, int readTimeout, Charset charset) {

        StringBuffer inputLine = new StringBuffer();

        try {
            HttpURLConnection urlConnection = (HttpURLConnection)new URL(url).openConnection();

            HttpURLConnection.setFollowRedirects(true);
            urlConnection.setConnectTimeout(connectTimeout);
            urlConnection.setReadTimeout(readTimeout);
            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            urlConnection.setRequestProperty("Accept",
                "text/vnd.wap.wml,text/html, application/xml;q=0.9, application/xhtml+xml;q=0.9, image/png, image/jpeg, image/gif, image/x-xbitmap, */*;q=0.1");

            BufferedReader in;

            if ("gzip".equalsIgnoreCase(urlConnection.getContentEncoding())) {
                in = new BufferedReader(
                    new InputStreamReader(new GZIPInputStream(urlConnection.getInputStream()), charset));
            } else {
                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), charset));
            }
            String str;
            while ((str = in.readLine()) != null) {
                inputLine.append(str).append("\r\n");
            }
            in.close();
        } catch (Exception e) {
            log.error("", e);
        }
        return inputLine.toString();
    }

    /**
     * 判断URL是否包含协议声明,若无则使用HTTP协议
     *
     * @param url
     * @return
     */
    public static String parseTargetUrl(String url) {

        if (!Pattern.matches("\\w+://.+", url)) {
            url = "http://" + url;
        }
        return url;

    }

    /**
     * 向服务器上传base64加密的string文件
     *
     * @param fileContent
     *            文件内容
     * @param targetdir
     *            目标文件夹
     * @param fileName
     *            文件名
     * @throws IOException
     */
    public static void uploadFile(String fileContent, String targetdir, String fileName) throws IOException {

        // 看是否有上传的文件夹否则新建
        File file = new File(targetdir + "/" + fileName);
        File dir = file.getParentFile();

        if ((!dir.exists()) || !(dir.isDirectory())) {
            dir.mkdirs();
        }

        try (InputStream in = new ByteArrayInputStream(Base64.getDecoder().decode(fileContent));
            OutputStream out = new FileOutputStream(file)) {
            int read = 0;
            byte[] buffer = new byte[1024];
            while ((read = in.read(buffer, 0, 1024)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 将文件转成base64 字符串
     *
     * @param path
     * @return
     * @author Johnson.Jia
     * @date 2017年2月27日 下午5:51:05
     */
    public static String encodeBase64File(String path) {
        File file = new File(path);
        try (FileInputStream inputFile = new FileInputStream(file)) {
            byte[] buffer = new byte[(int)file.length()];
            inputFile.read(buffer);
            return new String(Base64.getEncoder().encode(buffer));
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 获取Web-inf 下的 指定 路径
     *
     * @return
     */
    public static String getConfPath(String path) {
        String configpath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        if (!StringUtils.isBlank(configpath)) {
            configpath = configpath.replace("classes/", path);
            configpath = configpath.substring(1, configpath.length());
        } else {
            configpath = "";
        }
        if (!configpath.startsWith("/")) {
            configpath = "/" + configpath;
        }
        return configpath;
    }

    /**
     * 根据 class 获取 web-inf 下的指定路径
     *
     * @param T
     * @param path
     * @return
     * @author Johnson.Jia
     * @date 2015年12月3日 下午6:06:28
     */
    public static String getConfPath(Class<?> T, String path) {
        String configpath = T.getResource("").getPath();
        if (!StringUtils.isBlank(configpath)) {
            configpath = configpath.substring(0, configpath.indexOf("classes/")) + path;
            configpath = configpath.substring(1);
        } else {
            configpath = "";
        }
        if (!configpath.startsWith("/")) {
            configpath = "/" + configpath;
        }
        return configpath;
    }

    public static void main(String[] args) throws Exception {
        String fileContent = encodeBase64File("G:/RTX_Files/贾言坤/VMware-workstation-full-11.0.0-2305329.1418091545.exe");
        uploadFile(fileContent, "G:/image/", "test.exe");
    }
}
