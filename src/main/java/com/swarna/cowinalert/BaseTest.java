package com.swarna.cowinalert;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

public class BaseTest {



        public static void sendmail (String data) {

            System.out.println("SimpleEmail Start");

            String smtpHostServer = "smtp.gmail.com";
            final String emailID = "info@example.com";
            final String password = "***";
            String receipent = null;
            if(!data.equals("Jaagte raho")) {
                 receipent = "tataighosh76@gmail.com,kumargauravgupta3@gmail.com,sudipcold@gmail.com" +
                        ",jalanprateek@gmail.com,supratik.debnath@gmail.com,sreemoyeec172@gmail.com," +
                        "prakruta78@gmail.com";
            } else {
                receipent = "tataighosh76@gmail.com,kumargauravgupta3@gmail.com,sudipcold@gmail.com";
            }

            Properties props = System.getProperties();

            props.put("mail.smtp.host", smtpHostServer);
            props.put("mail.smtp.starttls.enable",true);
            props.put("mail.smtp.port","587");
            props.put("mail.smtp.auth",true);



            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailID,password);
                }
            });

            EmailUtil.sendEmail(session, receipent, "SimpleEmail Testing Subject", data);
        }

}
