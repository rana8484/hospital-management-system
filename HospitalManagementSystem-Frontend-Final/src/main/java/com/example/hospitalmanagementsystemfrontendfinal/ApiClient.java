package com.example.hospitalmanagementsystemfrontendfinal;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class ApiClient {

    public static String executeAPI(String requestType, String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestType);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);

            if (!requestType.equals("GET")) {
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));

                try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                    wr.writeBytes(urlParameters);
                }
            }

            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static byte[] executeAPIForBytes(String requestType, String targetURL) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestType);
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);

            int status = connection.getResponseCode();
            if (status == 200) {
                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();

                byte[] data = new byte[4096];
                int nRead;
                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }

                buffer.flush();
                return buffer.toByteArray();
            } else {
                System.out.println("Error: HTTP status code = " + status);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }

    public static boolean executeMultipartAPI(String url, String email, String phone, byte[] imageData) {
        try {
            String boundary = "Boundary-" + System.currentTimeMillis();
            HttpRequest.BodyPublisher bodyPublisher = buildMultipartBody(email, phone, imageData, boundary);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .PUT(bodyPublisher)
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static HttpRequest.BodyPublisher buildMultipartBody(String email, String phone, byte[] imageData, String boundary) {
        var byteArrays = new ArrayList<byte[]>();
        String LINE_FEED = "\r\n";

        if (email != null) {
            byteArrays.add(("--" + boundary + LINE_FEED).getBytes());
            byteArrays.add(("Content-Disposition: form-data; name=\"email\"" + LINE_FEED + LINE_FEED).getBytes());
            byteArrays.add((email + LINE_FEED).getBytes());
        }

        if (phone != null) {
            byteArrays.add(("--" + boundary + LINE_FEED).getBytes());
            byteArrays.add(("Content-Disposition: form-data; name=\"phone\"" + LINE_FEED + LINE_FEED).getBytes());
            byteArrays.add((phone + LINE_FEED).getBytes());
        }

        if (imageData != null) {
            byteArrays.add(("--" + boundary + LINE_FEED).getBytes());
            byteArrays.add(("Content-Disposition: form-data; name=\"profilePhoto\"; filename=\"image.jpg\"" + LINE_FEED).getBytes());
            byteArrays.add(("Content-Type: image/jpeg" + LINE_FEED + LINE_FEED).getBytes());
            byteArrays.add(imageData);
            byteArrays.add(LINE_FEED.getBytes());
        }

        byteArrays.add(("--" + boundary + "--" + LINE_FEED).getBytes());

        return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
    }
}