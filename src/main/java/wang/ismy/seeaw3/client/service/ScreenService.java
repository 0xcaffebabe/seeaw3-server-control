package wang.ismy.seeaw3.client.service;

import com.aliyun.oss.OSSClient;
import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ScreenService {

    private  Robot robot ;

    private String endpoint = "";
    private String accessKeyId = "";
    private String accessKeySecret = "";

    public ScreenService() {

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public String screenAndUpload(){
        BufferedImage bfImage = null;
        try {

            bfImage = robot.createScreenCapture(new Rectangle(0, 0, 1366, 768));
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            ImageIO.write(bfImage,"png",byteArrayOutputStream);

            return upload(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String photographAndUpload(){
        Webcam webcam = Webcam.getDefault();
        Dimension dimension=new Dimension();
        dimension.setSize(640,480);
        webcam.setViewSize(dimension);
        webcam.open();


        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        try {
            ImageIO.write(webcam.getImage(), "PNG", byteArrayOutputStream);
            return upload(byteArrayOutputStream.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            webcam.close();
        }

        return "";
    }

    private String upload(byte[] bytes){


        String key = null;

        key="screen/"+System.currentTimeMillis();

        String back=".png";
        key+=back;


        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        String bucketName = "ismy1";
        ossClient.putObject(bucketName,key,new ByteArrayInputStream(bytes));

        ossClient.shutdown();

        return "https://"+ bucketName +"."+ endpoint +"/"+key;
    }
}
