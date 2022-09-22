package util;

import model.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonConverterTest {
    @Test
    void testJsonStringToMap() {
        Map<String,String> map = JsonConverter.jsonStringToMap("{\"a\":\"b\"}");
        assertEquals("b", map.get("a"));
    }

    @Test
    void stringify() {
        User user1 = new User("id1", "pw1", "name1", "email1");
        User user2 = new User("id2", "pw2", "name2", "email2");
        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);
        assertEquals("[{\"userId\":\"id1\",\"password\":\"pw1\",\"name\":\"name1\",\"email\":\"email1\"},{\"userId\":\"id2\",\"password\":\"pw2\",\"name\":\"name2\",\"email\":\"email2\"}]",
                JsonConverter.stringify(list));
    }
}