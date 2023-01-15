import java.io.DataOutputStream;
import java.net.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String allpostIds = refreshFeed();
        String[] separate = allpostIds.split("id\":\"");
        for (int x = 1; x < 100; x++) {
            try{
                String commentContent = createComment(x,separate);
                if(commentContent.toString().contains("1500")){
                    allpostIds = refreshFeed();
                    separate = allpostIds.split("id\":\"");
                    x=1;
                }
            }catch (Exception e){

            }
            Thread.sleep(5000);
        }
    }
    private static String refreshFeed() throws IOException {
        String url = "https://api.byte.co/feed/global";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setDoInput(true);
        con.setRequestProperty("User-Agent", "byte/0.2 (co.byte.video; build:145; iOS 13.3.0) Alamofire/4.9.1");
        con.setRequestProperty("Authorization", "");
        StringBuilder content;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            content = new StringBuilder();
            while ((line = in.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    private static String createComment(int x, String[] separate) throws IOException {
        String postId = separate[x].substring(0, separate[x].indexOf("\""));
        String url = "https://api.byte.co/post/id/" + postId + "/feedback/comment";
        String urlParameters = "{\"body\":\"Currently doing Follow 4 follow and like for like!"+"\",\"postID\":\"" + postId + "\"}";
        URL myurl = new URL(url);
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        HttpURLConnection con = (HttpURLConnection) myurl.openConnection();
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("User-Agent", "byte/0.2 (co.byte.video; build:145; iOS 13.3.0) Alamofire/4.9.1");
        con.setRequestProperty("Authorization", "");
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.write(postData);
        }
        StringBuilder content;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String line;
            content = new StringBuilder();
            while ((line = in.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
        }
        return content.toString();
    }
}
