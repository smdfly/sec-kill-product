package com.lanchong;

import static org.junit.Assert.assertTrue;

import com.lanchong.utils.MD5Util;
import org.junit.Test;

/**
 * Unit test for simple MainApplication.
 */
public class MainApplicationTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void testGetPasswor(){
        String saltDB = "1l2j3g";
        String calcPass = MD5Util.formPassToDBPass("83a1b4fcb2f8d3af5fabfe6562020e10", saltDB);
        System.out.println(calcPass);
    }

}
