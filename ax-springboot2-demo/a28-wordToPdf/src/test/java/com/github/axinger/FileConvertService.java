package com.github.axinger;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class FileConvertService {

    private final WebClient webClient;
    private final String kkFileViewUrl = "http://your-kkfileview-server/document/convert";

    public FileConvertService() {
        this.webClient = WebClient.builder().baseUrl(kkFileViewUrl).build();
    }

    //  Spring WebFlux 并且希望利用非阻塞的 WebClient
    public Mono<Void> convertWordToPdf(String wordFilePath, String pdfOutputPath) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.queryParam("source", wordFilePath).queryParam("outputType", "pdf").build())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .retrieve()
                .bodyToMono(DataBuffer.class)
                .doOnNext(dataBuffer -> saveDataBufferToFile(dataBuffer, pdfOutputPath))
                .then();
    }

    private void saveDataBufferToFile(DataBuffer dataBuffer, String filePath) {
        try (AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(java.nio.file.Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            fileChannel.write(dataBuffer.asByteBuffer().flip(), 0);
            System.out.println("转换成功，PDF 已保存至: " + filePath);
        } catch (IOException e) {
            System.err.println("写入文件时发生错误: " + e.getMessage());
        } finally {
            DataBufferUtils.release(dataBuffer);
        }
    }

    public void convertWordToPdf2(String wordFilePath, String pdfOutputPath) {

        RestTemplate restTemplate = new RestTemplate();


        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        body.add("source", wordFilePath);

        body.add("outputType", "pdf");


        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);


        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


        ResponseEntity<byte[]> response = restTemplate.postForEntity(kkFileViewUrl, requestEntity, byte[].class);


        if (response.getStatusCode().is2xxSuccessful()) {

            try (FileOutputStream fos = new FileOutputStream(pdfOutputPath)) {

                fos.write(Objects.requireNonNull(response.getBody()));

                System.out.println("转换成功，PDF 已保存至: " + pdfOutputPath);

            } catch (IOException e) {

                System.err.println("写入文件时发生错误: " + e.getMessage());

            }

        } else {

            System.err.println("文件转换失败，状态码: " + response.getStatusCode());

        }

    }
}
