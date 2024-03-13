package test0308.example.test.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * 로컬 환경에서의 경우 -> 스토리지를 proj 파일 내 작성하고 송 수신. (별로 안좋은 방법)
 * 배포 환경에서의 경우 -> 클라우드 스토리지 서비스(Amazon S3, Google Cloud Storage 등) 사용
 * 테스트 환경에서는 이렇게 해놓고, 배포환경에서 설정하도록.
 */

@Controller // 클래스 스프링 MVC 컨트롤러로 선언
public class FileDownloadController {

    @GetMapping("/download")
    // '/download' 경로로 GET 요청이 들어오면 메소드가 처리.
    public ResponseEntity<Resource> downloadFile() {
        try {
            // 파일 절대 경로 반환
            Path file = Paths.get("src/main/resources/static/a.txt.zip").toAbsolutePath().normalize();
            // 파일 경로로부터 리소스를 생성
            Resource resource = new UrlResource(file.toUri());

            // 파일이 존재하는 경우
            if (resource.exists()) {
                // 파일을 다운로드할 수 있는 경우 HTTP 응답을 생성.
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType("application/zip")) // 컨텐츠 타입을 지정.
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"") // 다운로드될 때의 파일 이름을 설정.
                        .body(resource); // 응답 본문에 파일 리소스를 포함.
            } else {
                // 파일이 존재하지 않는 경우, 404 Not Found 응답을 반환.
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // 어떠한 예외가 발생한 경우, 500 Internal Server Error 응답을 반환.
            return ResponseEntity.internalServerError().build();
        }
    }
}

