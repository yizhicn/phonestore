package phonestore.config;

import lombok.Data;
import org.springframework.stereotype.Component;

//支付宝沙箱支付配置类

@Data
@Component
public class AlipayConfig {
    //自己的appId
    public static String appId = "9021000124665788";
    //应用私有秘钥
    public static String appPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC9G/QYqDCI8n5duR4PmW9ZSviBsocxoHEeTVGBnPvqQyT2wTKbmh99f8ENJl+DIJZlveCSkushWXCoi7dpACMpcvB0et7lUIa1akUzeKhCgYnWYBbs52zGNiKgT/MtsODrVpAYobvQhO369WYLKXljlCfdq2fy9HnZSXstj/DLVg8n5+r5ttXbWHBzc1zmX39xdh+Jp8lwbPMAcTANVnAP3a07fbRZREWl0uEt2ZYZ8N8skhW4tPpuq8OCJ/5atBuDuOruIWucsqe/P4BQSwzNdZdhbDIfE2Du+Ehl54V2wSmRXfB+i5mDCGCIUsAdDPT0OzR05hDm0fkWgis6bXopAgMBAAECggEAHFa9DJjftRYJw8fC7nmCyzHF7cGwKsqtSOozRaYdxixLtMKc7953y5nCd1MByyeuAg8jQTPNEpFRpaBSWuTPYB2+VOY40spvdW44co8pP9YjrsF1jgjcLxPbuoRgn7865WlybI6IE6Zn90yg/xk2/KouEzTlfe4O+HXfhKNmZUH9LKo07bar9FJPbIJ4tQ+gdRFdKsLgJu1AYV2NYgVud3lGLXDOC++3r+XLmnPGWu+xkPi9fEc3oZzKATwqbSrjJV44BnfGt5v4Y/jGpNo8br/SHOAElAgqUbm28A2wEv0CQFdMifH6tuzA2raIh6EvBrEQH/gzNuTuPqNiA+1tUQKBgQDqoDyFLli9vuvqDox4Ga0ju95dMkUWDpPuv5Jqjs+pUWyjVun7lGJsw8soeD1+Fs0+9K0FMkM76lpRrtRrO2C1kfptSTrPE+oeoaKBj/8aaNkLr3+EhnC36gC4ICXsR+8qQIXyycdau1XA4QALts9X1a31ZbiiqQe/IUeBGPs6/QKBgQDOVjYVHtEv1PFDQFVOYaiYh9nA7jSrW94yI/18Bg0wZO+F2NJLgTGEOoor+CyIp04nRzAZLcYFFWTSdCcj1dyoHuhyoErOfnFrytJJBknhUdzvCvBDc6dRJtpMsBd1WYqRkPqwVYglALF01xEkEH0y+rviXZp2XybCWX2w2QWRnQKBgQC01BaQ1q2SYClW1SuyDKgCmHLDBVF1tHqTUZa5BaRK7PR71yqyuSQ6i5Rydml+WniJgu4uyRREg1RlDAevNa5NglquC/iFTOXuAclLbf3BDvnpJbN0ImGRfYpcXnqb5r0PpXcXLii1ZxiqdT8hyB15prhSH7nzs67CwhVoDCbkyQKBgEx77n4ztyz4zRm1riloz9sCeHgolEN0wU9qwdqIT7lBoPvHXGR6lkVCgdc8nh1ljvXGUJD0w7M8s+vC/IMvEiULl/RtcG1Mt3RScVJ6T0aDVtcRAfCu3TWapk7I81kfgNrml9ZnThkRQbZixkrNBeuwv5ZwRgJc0Pw3ufyk5YiNAoGAGzv9ZB/Va/g6HTi3MustVxgKthHEe8yCosyorPkCfxK3HddRq4Pw+q5PY/TG9YGZWOjgRKG6uaOlcV4tN3Rh1EeANzNJJvwZCfq6Uy5v1TKQk4L9Yt+4CiZP8fGLZXRzDJ8ahmo+YflgebpFzCmb2vBgh01bIZhIzR1asuAGHfE=";
    //支付宝公钥
    public static String alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyXOXODuXDHdQhW/V/6WgALvICNZptGhrNQqkNYS1gP60gpmdJRAdTdDn30UESW0uSHT4WhUCW9docJDRLe+6+/o9v0FeRoFLNS+nJo+DHRN1/oJmTXxSF+C58Uu7n8SpH6UPYBb/NSJwFCW1t1lYzEylrJpH+CfYMlDjVv/qWGiTkFlln+LaSWPxIv3XNaddBJ35JoR7KlZWuRMEaM8dcfs6BGQsfyeyuLnEeHfTpmydxxvHOYYfSnlq3/JNffEtrKsAHEPsvaHUw+iUcyECE/zJ4kHg4JjD5D1K7AQyOV+LFkwDJIkJctjaeCHNtrbzf3VeDN1pnWf0OcVv7aoDuwIDAQAB";
    //异步回调地址
    public static String notifyUrl = "http://localhost:8080/alipay/notifyNotice";
    //同步回调地址
    public static String returnUrl = "http://localhost:8080/alipay/returnNotice";
    //推荐使用这个秘钥
    public static String signType = "RSA2";
    //使用的编码格式
    public static String charset = "utf-8";
    //支付宝默认网关
    public static String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

}
