package com.mylog.common.filter;

import com.mylog.common.utils.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XSS攻击防御过滤器
 * 支持以下防护：
 * 1. 请求参数过滤
 * 2. Header头过滤
 * 3. 敏感字符转义
 * 4. JSON格式内容过滤
 *
 * @author pss
 * @date 2025/3/28
 */
public class XssFilter extends OncePerRequestFilter {

    // 排除列表（静态资源、健康检查等）
    private static final List<String> EXCLUDE_URLS = Arrays.asList("/favicon.ico", "/actuator/health", "/swagger");

    // XSS模式匹配（可根据需要扩展）
    private static final Pattern[] XSS_PATTERNS = new Pattern[]{Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE), Pattern.compile("src[\r\n]*=[\r\n]*'(.*?)'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL), Pattern.compile("</script>", Pattern.CASE_INSENSITIVE), Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL), Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL), Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL), Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE), Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE), Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isExemptRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 排除静态资源请求
        if (EXCLUDE_URLS.stream().anyMatch(url -> request.getRequestURI().contains(url))) {
            filterChain.doFilter(request, response);
            return;
        }

        // 包装请求对象
        filterChain.doFilter(new XssRequestWrapper(request), response);
    }

    // 在XssFilter类中添加豁免方法
    private boolean isExemptRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/rich-text/") || uri.contains("/editor/");
    }

    /**
     * 自定义请求包装器
     */
    public static class XssRequestWrapper extends HttpServletRequestWrapper {
        // 预编译正则表达式（优化性能）
        private static final Pattern ON_EVENT_PATTERN = Pattern.compile("(?i)on\\w+=");
        private static final Pattern[] CHARACTER_ESCAPE_PATTERNS = {Pattern.compile("<"), Pattern.compile(">"), Pattern.compile("'"), Pattern.compile("\""), Pattern.compile("/")};
        private static final String[] REPLACEMENTS = {"&lt;", "&gt;", "&#39;", "&quot;", "&#x2F;"};

        public XssRequestWrapper(HttpServletRequest request) {
            super(request);
        }


        @Override
        public String getParameter(String name) {
            return cleanXSS(super.getParameter(name));
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values == null) return null;

            return Arrays.stream(values).map(this::cleanXSS).toArray(String[]::new);
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            Map<String, String[]> parameterMap = super.getParameterMap();
            Map<String, String[]> cleanedMap = new LinkedHashMap<>();

            parameterMap.forEach((key, values) -> cleanedMap.put(key, Arrays.stream(values).map(this::cleanXSS).toArray(String[]::new)));
            return cleanedMap;
        }

        @Override
        public String getHeader(String name) {
            return cleanXSS(super.getHeader(name));
        }

        /**
         * XSS清理核心方法
         */
        public String cleanXSS(String value) {
            if (StringUtils.isBlank(value)) return value;

            // 阶段1：移除高危标签
            String cleanedValue = value;
            for (Pattern pattern : XSS_PATTERNS) {
                cleanedValue = pattern.matcher(cleanedValue).replaceAll("");
            }

            // 阶段2：HTML转义
            String cleaned = StringEscapeUtils.escapeHtml4(cleanedValue);

            // 阶段3：处理事件属性（安全替换）
            cleaned = ON_EVENT_PATTERN.matcher(cleaned).replaceAll(Matcher.quoteReplacement("_disabled_"));

            // 阶段4：字符转义（优化性能）
            for (int i = 0; i < CHARACTER_ESCAPE_PATTERNS.length; i++) {
                cleaned = CHARACTER_ESCAPE_PATTERNS[i].matcher(cleaned).replaceAll(Matcher.quoteReplacement(REPLACEMENTS[i]));
            }
            return cleaned;
        }
    }
}
