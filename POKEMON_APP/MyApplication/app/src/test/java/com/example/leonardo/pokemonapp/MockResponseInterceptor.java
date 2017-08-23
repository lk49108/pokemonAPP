package com.example.leonardo.pokemonapp;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MockResponseInterceptor implements Interceptor {

    private static final int BUFFER_SIZE = 1024 * 4;

    private Context context;
    private String scenario;

    public MockResponseInterceptor(Context context, String scenario) {
        this.context = context.getApplicationContext();
        this.scenario = scenario;
    }

    public MockResponseInterceptor(Context context) {
        this.context = context.getApplicationContext();
    }

    public void setScenario(String scenario) {
        this.scenario = scenario;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        // Get resource ID for mock response file.
        String fileName = getFilename(chain.request(), scenario);
        int resourceId = getResourceId(fileName);
        if (resourceId == 0) {
            // Attempt to fallback to default mock response file.
            fileName = getFilename(chain.request(), null);
            resourceId = getResourceId(fileName);
            if (resourceId == 0) {
                throw new IOException("Could not find res/raw/" + fileName);
            }
        }

        // Get input stream and mime type for mock response file.
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        String mimeType = URLConnection.guessContentTypeFromStream(inputStream);
        if (mimeType == null) {
            mimeType = "application/json";
        }

        // Build and return mock response.
        return new Response.Builder()
                .addHeader("content-type", mimeType)
                .body(ResponseBody.create(MediaType.parse(mimeType), toByteArray(inputStream)))
                .code(200)
                .message("{\n" +
                        "  \"data\": [\n" +
                        "    {\n" +
                        "      \"id\": \"35\",\n" +
                        "      \"type\": \"pokemons\",\n" +
                        "      \"attributes\": {\n" +
                        "        \"name\": \"Pikachu\",\n" +
                        "        \"base-experience\": 20,\n" +
                        "        \"is-default\": true,\n" +
                        "        \"order\": 35,\n" +
                        "        \"height\": 1.5,\n" +
                        "        \"weight\": 15.2,\n" +
                        "        \"created-at\": \"2016-08-02T09:10:40.553Z\",\n" +
                        "        \"updated-at\": \"2016-08-02T09:10:40.553Z\",\n" +
                        "        \"image-url\": null,\n" +
                        "        \"description\": \"Some pokemon description\",\n" +
                        "        \"total-vote-count\": 0,\n" +
                        "        \"voted-on\": 0,\n" +
                        "        \"gender\": \"Unknown\"\n" +
                        "      },\n" +
                        "      \"relationships\": {\n" +
                        "        \"types\": {\n" +
                        "          \"data\": [\n" +
                        "            {\n" +
                        "              \"id\": \"21\",\n" +
                        "              \"type\": \"types\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "              \"id\": \"22\",\n" +
                        "              \"type\": \"types\"\n" +
                        "            }\n" +
                        "          ]\n" +
                        "        },\n" +
                        "        \"comments\": {\n" +
                        "          \"data\": []\n" +
                        "        },\n" +
                        "        \"moves\": {\n" +
                        "          \"data\": [\n" +
                        "            {\n" +
                        "              \"id\": \"110\",\n" +
                        "              \"type\": \"moves\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "              \"id\": \"111\",\n" +
                        "              \"type\": \"moves\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "              \"id\": \"112\",\n" +
                        "              \"type\": \"moves\"\n" +
                        "            }\n" +
                        "          ]\n" +
                        "        }\n" +
                        "      }\n" +
                        "    }]}")
                .protocol(Protocol.HTTP_1_0)
                .request(chain.request())
                .build();
    }

    private String getFilename(Request request, String scenario) throws IOException {
        String requestedMethod = request.method();
        String prefix = scenario == null ? "" : scenario + "_";
        String filename = prefix + requestedMethod + request.url().url().getPath();
        filename = filename.replace("/", "_").replace("-", "_").toLowerCase();
        return filename;
    }

    private int getResourceId(String filename) {
        return context.getResources().getIdentifier(filename, "raw", context.getPackageName());
    }

    private static byte[] toByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            byte[] b = new byte[BUFFER_SIZE];
            int n = 0;
            while ((n = is.read(b)) != -1) {
                output.write(b, 0, n);
            }
            return output.toByteArray();
        } finally {
            output.close();
        }
    }
}