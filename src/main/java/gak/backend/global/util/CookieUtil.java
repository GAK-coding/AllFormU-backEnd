package gak.backend.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;
import java.util.Optional;

public class CookieUtil {

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length>0){
            for(Cookie cookie : cookies){
                if(name.equals(cookie.getName())){
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name){
        Cookie[] cookies = request.getCookies();

        if(cookies != null && cookies.length>0){
            for(Cookie cookie : cookies){
                if(name.equals(cookie.getName())){
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object object){
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

//    public static <T> T deserialize(Cookie cookie, Class<T> cls){
//        return cls.cast(
//                SerializationUtils.deserialize(
//                        Base64.getUrlDecoder().decode(cookie.getValue())
//                )
//        );
//    }
public static <T> T deserialize(Cookie cookie, Class<T> cls) {
    try {
        byte[] serializedData = Base64.getUrlDecoder().decode(cookie.getValue());
        ByteArrayInputStream byteStream = new ByteArrayInputStream(serializedData);
        try (ObjectInputStream objectStream = new ObjectInputStream(byteStream)) {
            Object deserializedObject = objectStream.readObject();
            return cls.cast(deserializedObject);
        }
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
    return null;
}
}
