package com.enigma.bookstore.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


@Component
public class EmailTemplateGenerator {
    @Autowired
    HttpServletRequest httpServletRequest;

    public String getHeader(String userName) {
        return "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN'\n" +
                "        'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>\n" +
                "<html xmlns:th='http://www.thymeleaf.org' xmlns='http://www.w3.org/1999/xhtml'>\n" +
                "<head>\n" +
                "    <title>Your Order has been placed successfully</title>\n" +
                "\n" +
                "    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>\n" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'/>\n" +
                "\n" +
                "    <style>\n" +
                "        .column {\n" +
                "            box-sizing: border-box;\n" +

                "            padding: 3%;\n" +
                "            float: left;\n" +
                "            width: 50%;\n" +
                "            height: 160px;\n" +
                "        }\n" +
                "\n" +
                ".bookName {\n" +
                "           display: block;\n" +
                "            margin-top: 1.33em;\n" +
                "           margin-bottom: 1.33em;\n" +
                "           margin-left: 0;\n" +
                "           margin-right: 0;\n" +
                "        }\n" +
                "\n" +
                "        .row:after {\n" +
                "            content: '';\n" +
                "            display: table;\n" +
                "            clear: both;\n" +
                "            margin-bottom: 2%;\n" +
                "        }\n" +
                "\n" +
                "        .subtable {\n" +
                "            float: right;\n" +
                "            width: 38%;\n" +
                "        }\n" +
                "\n" +
                "        .ordertb {\n" +
                "            width: 60%;\n" +
                "        }\n" +
                "\n" +
                "        .itemstb {\n" +
                "            width: 80%;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        .itemtr {\n" +
                "            height: 150px;\n" +
                "        }\n" +
                "\n" +
                "        .itemtd {\n" +
                "            width: 30%;\n" +
                "        }\n" +
                "\n" +
                "        .deli{\n" +
                "            margin-top: 4%;\n" +
                "        }\n" +
                "        .containersubtb{\n" +
                "            width: 103%;\n" +
                "            margin-bottom:3%;\n" +
                "        }\n" +
                "        .thanksMessage {\n" +
                "           width:80%;\n" +
                "            height:auto;\n" +
                "            font-family:Segoe Script;\n" +
                "            color: #b90f4b ;\n" +
                "        }\n" +
                "        .card {\n" +
                "            box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2);\n" +
                "            margin-left: 10%;\n" +
                "            margin-top: 2%;\n" +
                "            width: 80%;\n" +
                "        }\n" +
                "\n" +
                "        .navbar {\n" +
                "            width: 100%;\n" +
                "            display: flex;\n" +
                "            background-color: #b90f4b;\n" +
                "            overflow: auto;\n" +
                "        }\n" +
                "\n" +
                "        .linkButton {\n" +
                "            padding: 0.6em 0.7em;\n" +
                "            color: white;\n" +
                "            background-color: #b90f4b;\n" +
                "            margin: auto;\n" +
                "            border: none;\n" +
                "            display:flex;\n" +
                "            border-radius: 6px;\n" +
                "        }\n" +

                "        .navbar p {\n" +
                "            float: left;\n" +
                "            margin-left: 3%;\n" +
                "            padding: 8px;\n" +
                "            color: white;\n" +
                "            text-decoration: none;\n" +
                "            font-size: 20px;\n" +
                "        }\n" +
                "        .headerName{\n" +
                "            /*margin-left: 1%;*/\n" +
                "            padding: 16px 1px 16px 3px;\n" +
                "            color: white;\n" +
                "            text-decoration: none;\n" +
                "            font-size: 20px;\n" +
                "        }\n" +
                "        .logo {\n" +
                "            fill: #fff;\n" +
                "            padding: 16px 3px 16px 16px;\n" +
                "            width: 24px;\n" +
                "            height: 24px;\n" +
                "            /*text-align: center;*/\n" +
                "            /*vertical-align: center;*/\n" +
                "\n" +
                "        }\n" +
                "\n" +
                "        .card:hover {\n" +
                "            box-shadow: 0 8px 16px 0 rgba(0, 0, 0, 0.2);\n" +
                "        }\n" +
                "\n" +
                "        .container {\n" +
                "            padding: 5px 24px;\n" +
                "        }\n" +
                "\n" +
                "        @media only screen and (max-width: 768px) {\n" +
                "            .card {\n" +
                "                width: 100%;\n" +
                "                margin-left: 0.5%;\n" +
                "            }\n" +
                "            .column {\n" +
                "                box-sizing: border-box;\n" +
                "                padding: 3%;\n" +
                "                float: left;\n" +
                "                width: 50%;\n" +
                "                height: 200px;\n" +
                "            }\n" +
                "            .subtable {\n" +
                "                float: right;\n" +
                "                width: 100%;\n" +
                "            }\n" +
                "            .ordertb {\n" +
                "                width: 40%;\n" +
                "            }\n" +
                "            .itemstb {\n" +
                "                width: 100%;\n" +
                "                text-align: center;\n" +
                "            }\n" +
                "            .itemtr {\n" +
                "                height: 120px;\n" +
                "            }\n" +
                "            .itemtd {\n" +
                "                width: 22%;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        @media only screen and (min-device-width: 769px) and (max-device-width: 1024px) and (-webkit-min-device-pixel-ratio: 1) {\n" +
                "            .card {\n" +
                "                width: 90%;\n" +
                "                margin-left: 5%;\n" +
                "            }\n" +
                "            .column {\n" +
                "                box-sizing: border-box;\n" +
                "                padding: 3%;\n" +
                "                float: left;\n" +
                "                width: 50%;\n" +
                "                height: 170px;\n" +
                "            }\n" +
                "            .subtable {\n" +
                "                float: right;\n" +
                "                width: 55%;\n" +
                "            }\n" +
                "            .ordertb {\n" +
                "                width: 80%;\n" +
                "            }\n" +
                "            .itemstb {\n" +
                "                width: 100%;\n" +
                "                text-align: center;\n" +
                "            }\n" +
                "            .itemtr {\n" +
                "                height: 120px;\n" +
                "            }\n" +
                "            .itemtd {\n" +
                "                width: 22%;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        @media only screen and (min-device-width: 1025px) and (max-device-width: 2000px) and (-webkit-min-device-pixel-ratio: 1) {\n" +
                "            .card {\n" +
                "                width: 80%;\n" +
                "                margin-left: 10%;\n" +
                "            }\n" +
                "            .column {\n" +
                "                box-sizing: border-box;\n" +
                "                padding: 3%;\n" +
                "                float: left;\n" +
                "                width: 50%;\n" +
                "                height: 180px;\n" +
                "            }\n" +
                "            .ordertb {\n" +
                "                width: 50%;\n" +
                "            }\n" +
                "            .itemstb {\n" +
                "                width: 80%;\n" +
                "                text-align: center;\n" +
                "            }\n" +
                "            .itemtr {\n" +
                "                height: 120px;\n" +
                "            }\n" +
                "            .itemtd {\n" +
                "                width: 25%;\n" +
                "            }\n" +
                "            .subtable {\n" +
                "                float: right;\n" +
                "                width: 25%;\n" +
                "            }\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<div class='card'>\n" +
                "\n" +
                "    <div class='navbar' style='background-color:#b90f4b'>\n" +
                "        <img class='logo' src='cid:bookStoreLogo' />" +
                "        <div class='headerName'>\n" +
                "            e BookStore\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class='container' style='background-color:#f5f5f5'>\n" +
                "        <h4>Hello <b> " + userName + " </b></h4>\n";
    }

    public String getFooter() {
        return "        <div class='deli'>\n" +
                "            <p>We hope to see you again soon.</p>\n" +
                "            <h1 class='thanksMessage'><b align='center'>Thank you for\n" +
                "                choosing us</b></h1>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "\n" +
                "</html>\n" +
                "\n";
    }

    public String getVerifyEmailTemplate(String generateToken) {
        return "        <p>Your account has been created successfully , You are one step away from using our services \n" +
                "           so please first verify your account to use our service.</p>\n" +
                "        <p>If you have any questions,Mail us at bookstore.engima@gmail.com</p>\n" +
                "\n" +
                "\n" +
                "        <div style='display: flex;border:none; align-items: center;justify-content;margin-bottom:2%;margin-top:2%'>\n" +
                "<a style='text-decoration: none;border:none;' href='" + httpServletRequest.getHeader("origin") + "/verify/email/?token=" + generateToken + "'>"
                + "<button class='linkButton'>verify Your Email</button>" +
                "</a>" +
                "        </div>\n" +
                "\n";
    }
}
