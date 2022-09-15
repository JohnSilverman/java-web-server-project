package util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

class HttpStringUtilTest {
    @Test
    void parseQueryString() {
        Map<String,String> testResult = HttpStringUtil.parseQueryString("a=1&b=2&c=3");
        assertEquals("1",testResult.get("a"));
        assertEquals("2",testResult.get("b"));
        assertEquals("3",testResult.get("c"));
    }

    @Test
    void inputStreamToLines() {
        String inputString = "abc\r\nContent-Length: 7\r\nghi\r\n\r\njklmnop";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        List<String> testResult = HttpStringUtil.inputStreamToLines(inputStream);

        for(String str : testResult) System.out.println(str);

        assertEquals( 5,testResult.size());
        assertEquals( "abc",testResult.get(0));
        assertEquals("",testResult.get(3));
    }


}