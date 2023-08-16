package com.ceyloncab.otpmgtservice.external.serviceimpl;

import com.ceyloncab.otpmgtservice.domain.boundary.SendSMSInterface;
import com.ceyloncab.otpmgtservice.domain.utils.Constants;
import com.ceyloncab.otpmgtservice.external.dto.*;
import com.ceyloncab.otpmgtservice.external.exception.ExternalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * This is external service class for send otp as SMS message
 */
@Slf4j
@Service
public class SendSMSService implements SendSMSInterface {
    public static final String METHOD_NAME = "OTP SEND";

    @Value("${sms-messaging.senderUsername}")
    private String senderUsername;

    @Value("${sms-messaging.senderPassword}")
    private String senderPassword;
    @Value("${sms-messaging.message-text}")
    private String text;
    @Value("${sms-messaging.senderName}")
    private String senderName;

    @Autowired
    private ObjectMapper objectMapper;
    TokenBody tokenBody;
    String token="";

    @PostConstruct
    public void initializer(){
        tokenBody = new TokenBody();
        tokenBody.setUsername(senderUsername);
        tokenBody.setPassword(senderPassword);
        token = getToken();
    }

    /**
     * Call external API - Send OTP SMS
     *
     * @param msisdn:        mobile number
     * @param otp:           generated otp
     * @return sendStatus
     */
    @Override
    public void sendOTP(String msisdn, String otp) {

        reInitializeToken();
        SendTextBody sendTextBody = new SendTextBody();
        // set your number list here
        sendTextBody.setMsisdn(setMsisdns(new String[]{msisdn.substring(2)}));
        // set your source address here
        sendTextBody.setSourceAddress(senderName);
        // set your message here
        sendTextBody.setMessage(String.format(text,otp));
        // set the transaction id which is unique id for each SMS submission
        sendTextBody.setTransaction_id(generateTransactionId());

        try {
            SendTextResponse smsResponse = sendText(sendTextBody, token);
            if("success".equalsIgnoreCase(smsResponse.getStatus())){
                log.info("Sms send success.Msisdn:{}",msisdn);
            } else if ("Authentication Token Expired".equalsIgnoreCase(smsResponse.getComment())) {
                token = getToken();
                if("".equalsIgnoreCase(token)){
                    throw new ExternalException(Constants.ResponseData.OTP_GENERATION_FAILED);
                }
                smsResponse = sendText(sendTextBody, token);
                if(Boolean.FALSE.equals("success".equalsIgnoreCase(smsResponse.getStatus()))){
                    throw new ExternalException(Constants.ResponseData.OTP_GENERATION_FAILED);
                }
                log.info("Retry-Sms send success.Msisdn:{}",msisdn);
            }else {
                log.error("Error occurred in send sms API.Error:{}",smsResponse.getComment());
                throw new ExternalException(Constants.ResponseData.OTP_GENERATION_FAILED);
            }
        }catch (IOException io){
            log.error("Error occurred in send sms API.Error:{}",io.getMessage(),io);
            throw new ExternalException(Constants.ResponseData.OTP_GENERATION_FAILED);
        }
    }

    private String getToken(){
        String token = "";
        try {
            TokenResponse tokenResponse = generateToken(tokenBody);
            if("success".equals(tokenResponse.getStatus())){
                token = tokenResponse.getToken();
            }
        }catch (IOException io){
            log.error("Error occurred while getting sms token.Error:{}",io.getMessage(),io);
        }
        return token;
    }
    
    private void reInitializeToken(){
        if("".equalsIgnoreCase(token)){
            token = getToken();
        }
    }

    private String generateTransactionId(){
        Random random = new Random();
        Integer rn = random.nextInt(1000) + 1;
        String id = new Date().getTime() + String.valueOf(rn);

        int idLength = id.length();

        if(idLength>19){
            id = id.substring(idLength-19);
        }
        return id;
    }

    private TokenResponse generateToken(TokenBody tokenBody) throws IOException {
        String url = "https://esms.dialog.lk/api/v1/login";
        String requestBody = this.objectMapper.writeValueAsString(tokenBody);
        Header contentType = new BasicHeader("Content-Type", "application/json");
        Header[] headers = new Header[]{contentType};
        return (TokenResponse)this.objectMapper.readValue(postRequest(url, requestBody, headers), TokenResponse.class);
    }

    private SendTextResponse sendText(SendTextBody sendTextBody, String token) throws IOException {
        String url = "https://esms.dialog.lk/api/v1/sms";
        String requestBody = this.objectMapper.writeValueAsString(sendTextBody);
        Header contentType = new BasicHeader("Content-Type", "application/json");
        Header authType = new BasicHeader("Authorization", "Bearer " + token);
        Header[] headers = new Header[]{contentType, authType};
        return (SendTextResponse)this.objectMapper.readValue(postRequest(url, requestBody, headers), SendTextResponse.class);
    }

    private List<Msisdn> setMsisdns(String[] mobiles) {
        List<Msisdn> msisdns = new ArrayList();
        String[] var3 = mobiles;
        int var4 = mobiles.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String mobile = var3[var5];
            msisdns.add(new Msisdn(mobile));
        }
        return msisdns;
    }

    private String postRequest(String url, String body, Header[] headers) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        String var10;
        try {
            HttpPost request = new HttpPost(url);
            request.setHeaders(headers);
            request.setEntity(new StringEntity(body, "UTF-8"));
            CloseableHttpResponse response = client.execute(request);
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();

            while(true) {
                String line;
                if ((line = bufReader.readLine()) == null) {
                    var10 = builder.toString();
                    break;
                }

                builder.append(line);
                builder.append(System.lineSeparator());
            }
        } catch (Throwable var12) {
            if (client != null) {
                try {
                    client.close();
                } catch (Throwable var11) {
                    var12.addSuppressed(var11);
                }
            }
            throw var12;
        }
        if (client != null) {
            client.close();
        }
        return var10;
    }
}
