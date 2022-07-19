package com.hcc;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        //加密测试
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwork = "123456";
        boolean i = encoder.matches(passwork,"$2a$10$2HzoGAWw5vQVrlzdXXdfMeHtEvPXs3zqpJdXSIMVRVXV4WonnRFkK");

        System.out.println(i);
    }
}
