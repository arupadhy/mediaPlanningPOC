package com.sapient.ai.media.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class WebUtil
{
    public static String decode(String parameterValue) throws UnsupportedEncodingException
    {
        return URLDecoder.decode(parameterValue, "UTF-8");
    }
}
