package io.github.GrbavaCigla.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class JSON<T> implements Importer<T>, Exporter<T> {

    protected abstract Map<String, Object> toObject(T item);

    protected abstract T fromObject(Map<String, String> fields);

    @Override
    public void dump(BufferedWriter wr, List<T> data) throws IOException {
        wr.append("[");
        for (int i = 0; i < data.size(); i++) {
            wr.newLine();
            wr.append("  {");
            Map<String, Object> obj = toObject(data.get(i));
            int j = 0;
            for (Map.Entry<String, Object> entry : obj.entrySet()) {
                if (j++ > 0) wr.append(", ");
                wr.append("\"").append(escape(entry.getKey())).append("\": ");
                Object val = entry.getValue();
                if (val instanceof Number) {
                    wr.append(val.toString());
                } else {
                    wr.append("\"").append(escape(val.toString())).append("\"");
                }
            }
            wr.append(i < data.size() - 1 ? "}," : "}");
        }
        wr.newLine();
        wr.append("]");
        wr.flush();
    }

    @Override
    public List<T> load(BufferedReader rd) throws IOException {
        List<T> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line.trim());
        }
        String content = sb.toString().trim();
        if (content.startsWith("[") && content.endsWith("]")) {
            content = content.substring(1, content.length() - 1).trim();
        }
        int pos = 0;
        while (pos < content.length()) {
            int start = content.indexOf('{', pos);
            if (start == -1) break;
            int end = content.indexOf('}', start);
            if (end == -1) break;
            result.add(fromObject(parseObject(content.substring(start + 1, end))));
            pos = end + 1;
        }
        return result;
    }

    private Map<String, String> parseObject(String obj) {
        Map<String, String> map = new HashMap<>();
        Pattern p = Pattern.compile(
            "\"((?:[^\"\\\\]|\\\\.)+)\"\\s*:\\s*(?:\"((?:[^\"\\\\]|\\\\.)*)\"|([^,}\\s]+))");
        Matcher m = p.matcher(obj);
        while (m.find()) {
            String key = unescape(m.group(1));
            String value = m.group(2) != null ? unescape(m.group(2)) : m.group(3);
            map.put(key, value);
        }
        return map;
    }

    private String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String unescape(String s) {
        return s.replace("\\\"", "\"").replace("\\\\", "\\");
    }
}
